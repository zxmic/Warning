package all.certificatio;

import all.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import java.util.Date;
import java.text.SimpleDateFormat;

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
            String identity = tokenE.getUserid();

            if (identity.length() != 18) {
                System.out.println("0000-00-00");
                return json.toString();

            } else {
                int i, n = 0;
                for (i = 0; i < 17; i++) {
                    if (identity.charAt(i) >= '0' && identity.charAt(i) <= '9') {
                        n++;
                    } else
                        break;
                }

                if (n <= 16 || !(identity.charAt(17) >= '0' && identity.charAt(17) <= '9' || identity.charAt(17) >= 'a' && identity.charAt(17) <= 'z')) {
                    System.out.println("0000-00-00");
                    return json.toString();
                }else {
                    int year, month, day;
                    year = Integer.valueOf(identity.substring(6, 10));
                    month = Integer.valueOf(identity.substring(10, 12));
                    day = Integer.valueOf(identity.substring(12, 14));

                    String strDate = identity.substring(6, 14);
                    // 准备第一个模板，从字符串中提取出日期数字
                    String pat1 = "yyyyMMdd";
                    // 准备第二个模板，将提取后的日期数字变为指定的格式
                    String pat2 = "yyyy-MM-dd";
                    SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);        // 实例化模板对象
                    SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);        // 实例化模板对象
                    Date d = null;
                    try {
                        d = sdf1.parse(strDate);   // 将给定的字符串中的日期提取出来
                    } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
                        e.printStackTrace();       // 打印异常信息
                    }
                    String r = sdf1.format(d);
                    if (r.equals(strDate)) {
                        //校验算法
                        int j, m, sum = 0, mod;
                        int[] k = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                        for (j = 0; j < 17; j++) {
                            m = Integer.valueOf(identity.substring(j, j + 1));
                            sum += k[j] * m;
                        }

                        mod = sum % 11;
                        String mods = "10x98765432";
                        if (identity.charAt(17) == mods.charAt(mod)) {
                            json.put("id",sdf2.format(d));
                            json.put("token",token);
                            json.put("msg","Success_identify");
                            //System.out.println(sdf2.format(d));// 将日期变为新的格式
                        }else{

                            System.out.println("0000-00-00");
                            return json.toString();
                            }
                    } else {
                        System.out.println("0000-00-00");
                        return json.toString();
                    }

                }
            }


        } catch (JSONException e) {
            json.put("msg", "Attack!");
            return json.toString();
        }
        return json.toString();

    }

}
