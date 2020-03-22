package all.login.other;


import all.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

public class Login {
    private Jedis jedis;
    private String userid;
    private String token;

    public Login(){
        jedis = RedisUtil.getJedis();
        //jedis.auth("123456");
        userid="userid";
        token="token";
    }


    public String loginM(String data){
        //data中含有phoneNumber和password
        JSONObject json = new JSONObject();
        User user=null;
        try{
            user= JSON.parseObject(data,User.class);
//            JSONObject jsonObject = JSONObject.parseObject(data);
//            user = JSON.toJavaObject(jsonObject,User.class );

//            JSONObject jsonObject = JSONObject.parseObject(data);
//            user = (User) JSONObject.parseObject(String.valueOf(jsonObject), User.class);
//            user = jsonObject.getObject()
//            String phonenumber=jsonObject.getString("phoneNumber");
//            String password=jsonObject.getString("password");
//            user.setPassword(password);
//            user.setPhoneNumber(phonenumber);
        }catch (JSONException e){
            e.printStackTrace();
            json.put("msg","Attack!");
            return json.toString();
        }


//        //字符替换
//        user.setPhoneNumber(user.getPhoneNumber().replaceAll("'",""));

        if(!user.getPhoneNumber().equals("phoneNumber")&&!user.getPassword().equals("password")){
            //前端登录界面传进json
            //1、在redis中的hashmap查询phoneNumber是否存在，找到对应userid，且判断redis密码是否正确
            int i1=lookForInHashmap(user);
            switch (i1) {
                //3、该用户没有注册过，跳转到注册界面。
                case 4:
                    json.put("msg", "Not_Register");
                    return json.toString();
                //4、用户名密码正确
                case 1:
                    //把正确账号密码保存token
                    token = Token.saveTokenToRedis(jedis,userid);
                    json.put("userid",userid);
                    json.put("token", token);
                    json.put("msg", "Success_Login");
                    return json.toString();
                //5、redis中密码错误，重新登录
                case 2:
                    json.put("userid",userid);
                    //json.put("token", token);
                    json.put("msg", "WrongPassword");
                    return json.toString();

                //6、用户注册过，但是redis中不存在账号密码，查询数据库
                case 3:
                    int i2 = lookForInMysql(user);
                    switch (i2) {
                        //7、数据库查询账号密码正确
                        case 11:
                            //把正确账号密码保存token
                            token = Token.saveTokenToRedis(jedis,userid);
                            //把正确账号密码保存redis
                            saveRecordToRedis(user);
                            json.put("userid",userid);
                            json.put("token", token);
                            json.put("msg", "Success_Login");
                            return json.toString();
                        //8、数据库查询密码错误
                        case 12:
                            json.put("userid",userid);
                            json.put("msg", "WrongPassword");
                            return json.toString();
                        //9、数据库没有查询到信息，显示注册
                        case 13:
                            json.put("msg", "Not_Register");
                            return json.toString();
                        default:
                            json.put("msg", "Wrong_Login");
                            return json.toString();
                    }
                default:
                    json.put("msg", "Wrong_Login");
                    return json.toString();
            }
        }else {
            json.put("msg","Lack_Info");
            return json.toString();
        }
    }

    private void saveRecordToRedis(User user) {
        jedis.set("p"+user.getPhoneNumber(),user.getPassword());
    }

    //1、在redis中的hashmap查询k是否存在，且判断redis密码是否正确
    private int lookForInHashmap(User user){
        //2、该用户有注册信息
        if(jedis.hexists("phonenumber",user.getPhoneNumber())){
            //在hashmap中找到userid
            userid=jedis.hget("phonenumber",user.getPhoneNumber());
            //user.setUserid(Integer.parseInt(userid));
            //查询redis是否存在账号密码
            if(jedis.exists("p"+user.getPhoneNumber())){
                //4、redis存在账号密码，且密码正确,登录成功
                if(jedis.get("p"+user.getPhoneNumber()).equals(user.getPassword())){
                    //"Login_OK"  1
                    return 1;
                }else {
                    //5、查询redis密码错误，登录失败，提示密码错误，重新登录
                    //"Login"   2
                    return 2;
                }
            }else {
                //6、用户注册过，但是redis中不存在账号密码，通过userid查询数据库
                //"LookForSql"  3
                return 3;
            }
        }else {
            //3、该用户没有注册过，跳转到注册界面。
            //"Register"  4
            return 4;
        }
    }

    //6、查询数据库，且判断数据库密码是否正确
    private int lookForInMysql(User user){
        LoginService loginService = new LoginService();
        User userMysql=loginService.selectUserByPhoneNumber(userid);
        //数据库中存在注册信息
        if(userMysql!=null){
            if(userMysql.getPassword().equals(user.getPassword())){
                //7、数据库用户名密码正确
                //"Login_OK"  11
                return 11;
            }else {
                //8、数据库查询密码错误，需要重新输入
                //"Login"  12
                return 12;
            }
        }else {
            //9、数据库中不存在注册信息，需要注册
            //System.out.println("数据库中没有找到phonenumber，请先注册");
            //"Register" 13
            return 13;
        }
    }
}
