package all.register.other;


import all.login.other.User;
import all.util.RedisUtil;
import all.util.SendCodeUtil;
import all.util.SnowFlakeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register {
    Jedis jedis;
    RegisterService registerService;

    public Register(){
        jedis = RedisUtil.getJedis();
        registerService = new RegisterService();
    }

    //给已经注册过的用户法验证码  用于忘记密码的找回
    public String sendcodeForget(String phonenumber){
        JSONObject json = new JSONObject();
        //是11位的可用手机号码
        if(matchphone(phonenumber)){
            //该手机号为注册手机号
            if(jedis.hexists("phonenumber",phonenumber)){
                //五分钟内发过验证码
                if(jedis.exists("c"+phonenumber)){
                    json.put("msg", "Havesend");
                }else {
                    //可以发送验证码
                    jedis.setex("c" + phonenumber, 300, SendCodeUtil.sendCode(phonenumber));
                    json.put("msg", "Sendsuccess");
                }
            }else {
                //该手机号为没有注册手机号，不可用于密码找回
                json.put("msg","Newuser");
            }
        }else {
            //输入号码不是11位可用手机号
            json.put("msg","Wrongphone");
        }
        return json.toString();
    }

    //给没有注册过的号码发送验证码  用于注册
    public String sendcodeRegister(String phonenumber) {
        JSONObject json = new JSONObject();
        //如果是十一位正确的电话号码
        if(matchphone(phonenumber)){
            //存在注册记录
            if (jedis.hexists("phonenumber", phonenumber)) {
                //return "账号已存在";
                json.put("msg","Haveregister");
            } else {
                //存在验证码
                if (jedis.exists("c" + phonenumber)) {
                    // return "验证码已发送";
                    json.put("msg", "Havesend");
                } else {
                    //发送验证码
                    //验证码保存五分钟
                    jedis.setex("c" + phonenumber, 300, SendCodeUtil.sendCode(phonenumber));
                    json.put("msg", "Sendsuccess");
                }
            }
        }else {
            //不是十一位正确的电话号码
            json.put("msg", "Wrongphone");
        }
        return json.toString();
    }

    //用于修改绑定号码
    public String sendcodeChange(String userid,String phonenumber){
        JSONObject json = new JSONObject();
        json.put("userid",userid);
        //是11位可用电话号码
        if(matchphone(phonenumber)){
            //判断新号码是否没有注册过，没有注册过的电话号码可以用于绑定
            if(!jedis.hexists("phonenumber",phonenumber)){
                User user=registerService.userSelect(userid);
                //新旧号码不同
                if(!user.getPhoneNumber().equals(phonenumber)){
                    //可以发送验证码
                    //验证码保存五分钟
                    jedis.setex("c" + phonenumber, 300, SendCodeUtil.sendCode(phonenumber));
                    json.put("msg","Sendsuccess");
                }else {
                    //新旧号码相同
                    json.put("msg","Samephone");
                }

            }else {
                //新号码已经注册过了，就不能绑定了，不发送验证码
                json.put("msg","Havebend");
            }
        }else {
            //非十一位可用号码
            json.put("msg", "Wrongphone");
        }
        return json.toString();
    }

    //判断是否是可用电话号码
    private boolean matchphone(String phonenumber){
        boolean str= true;
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phonenumber.length() != 11){
            str=false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phonenumber);
            boolean isMatch = m.matches();
            if(!isMatch){
                str=false;
            }
        }
        return str;
    }

    //注册
    public String register(String phonenumber,String code0,String password) {
        Map<String, String> res = new HashMap<>();
        Long userid;
        if (jedis.hexists("phonenumber", phonenumber)) {
            //return "账号已存在";
            res.put("msg", "Haveregister");
        } else {
            //先验证验证码是否发送
            if (jedis.exists("c" + phonenumber)) {
                if (jedis.get("c" + phonenumber).equals(code0)) {
                    //写入数据库
                    userid = SnowFlakeUtil.getId();
                    boolean isAdd=registerService.userAdd(userid, phonenumber, password);
                    //数据库添加成功
                    if(isAdd){
                        jedis.hset("phonenumber",phonenumber, String.valueOf(userid));
                        jedis.set("p"+phonenumber,password);
                        boolean keyExist = jedis.exists("c"+phonenumber);
                        if (keyExist) {
                            jedis.del("c"+phonenumber);
                        }
                        res.put("msg","Registerok");
                    }

                } else {
                    //return "验证码错误";
                    res.put("msg", "Falsecode");
                }
            }
            else{
                //未发送验证码或验证码超过五分钟
                res.put("msg", "Resendcode");
            }
        }
        return JSON.toJSONString(res);
    }

    //忘记密码时 修改密码
    public String changePass(String phonenumber,String code ,String password){
        Map<String, String> res = new HashMap<>();
        boolean flag=false;
        //注册过的账号
        if (jedis.hexists("phonenumber", phonenumber)) {
            //code码是否一致
            if(jedis.exists("c"+phonenumber)){
                if(jedis.get("c"+phonenumber).equals(code)){
                    //可以修改密码
                    flag=registerService.phoneUpdatePass(phonenumber,password);
                }
                if(flag){
                    //修改成功
                    res.put("msg","Changesuccess");
                }else {
                    res.put("msg","Changefalse");
                }
            }else {
                res.put("msg","Unsamecode");
            }
        } else {
            res.put("msg","Noregister");
        }
        return JSON.toJSONString(res);
    }

    //修改绑定手机
    public String changePhone(String userid,String phonenumber,String code ){
        Map<String, String> res = new HashMap<>();
        res.put("userid",userid);
        //直接修改了
        boolean flag=false;
        if(jedis.exists("c"+phonenumber)){
            if(jedis.get("c"+phonenumber).equals(code)){
                flag=registerService.userUpdatePhone(userid,phonenumber);
            }else {
                res.put("msg","Falsecode");
            }
            if(flag){
                res.put("msg","Changesuccess");
            }else {
                res.put("msg","Changefalse");

            }
        }else {
            res.put("msg","Sendcode");
        }
        return JSON.toJSONString(res);
    }

}