package all.carmassage.other;

import all.util.RedisUtil;
import all.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
    final private Jedis jedis;
    private String token;
    private String cph = "cph";

    public Token() {
        jedis = RedisUtil.getJedis();
        token = "token";
    }


    public String getToken(String data) {
        JSONObject json = new JSONObject();
        TokenE tokenE = null;
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            tokenE = JSON.toJavaObject(jsonObject, TokenE.class);
            String cph=tokenE.getCph();

            Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5|WJ]{1}[A-Z0-9]{6}$");
            Matcher matcher = pattern.matcher(cph);
            if (!matcher.matches()) {
                System.out.println("车牌号格式不对！");
                return json.toString();
            }else{
                System.out.println("车牌号格式正确！");
            }
        } catch (JSONException e) {
            json.put("msg", "Attack!");
            return json.toString();
        }

        if(!tokenE.getCph().equals("cph")&&!tokenE.getToken().equals("token")){

            if(jedis.exists("token_"+tokenE.getCph())){
                //比较redis的token与传入token的值是否相同
                if(jedis.get("token_"+tokenE.getCph()).equals(tokenE.getToken())){
                    //redis存在token，比对成功后，免密成功
                    boolean keyExist = jedis.exists("token_"+tokenE.getCph());
                    token= TokenUtil.getToken(tokenE.getCph());
                    json.put("cph",tokenE.getCph());
                    json.put("token",token);
                    json.put("msg","Success_cph");
                    return json.toString();
                }else {
                    //redis存在token，比对失败后，免密失败，返回重新进入登录界面
                    json.put("cph",tokenE.getCph());
                    json.put("token",tokenE.getToken());
                    json.put("msg","Fail_cph");
                    return json.toString();
                }
            }else {
                //不存在token，表示登录过期，则重新输入用户名密码
                json.put("token",tokenE.getToken());
                json.put("msg","Fail_cph");
                json.put("cph",cph);
                return json.toString();
            }
        }
        else {
            json.put("msg","Fail_ckp");
            return json.toString();
        }
    }

    public static String saveTokenToRedis(Jedis jedis,String cph){
        //jedis1.auth("123456");
        boolean keyExist = jedis.exists("token_"+cph);
        if (keyExist) {
            jedis.del("token_"+cph);
        }
        String tokenstr= TokenUtil.getToken(cph);
        jedis.set("token_"+cph, tokenstr);
        return tokenstr;
    }


}
