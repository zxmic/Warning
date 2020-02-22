package all.register;

import all.register.other.Register;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registerall {
    //用于新号码注册
    public static String codeRegister(String phonenumberdata){
        //phonenumber
        Register register = new Register();
        JSONObject json = new JSONObject();
        String phonenumber;
        try{
            JSONObject jsonObject = JSON.parseObject(phonenumberdata);
            phonenumber = jsonObject.getString("phonenumber");
            return register.sendcodeRegister(phonenumber);
        }catch (JSONException e){
            json.put("msg","Wrong");
            return json.toString();
        }
    }

    //用于忘记密码找回
    public static String codeForget(String phonenumberdata){
        //phonenumber
        Register register = new Register();
        JSONObject json = new JSONObject();
        String phonenumber;
        try{
            JSONObject jsonObject = JSON.parseObject(phonenumberdata);
            phonenumber =jsonObject.getString("phonenumber");
            return register.sendcodeForget(phonenumber);
        }catch (JSONException e){
            json.put("msg","Wrong");
            return json.toString();
        }
    }

    //用于修改绑定号码
    public static String codeChange(String phonenumberdata){
        //userid phonenumber
        Map<String, String> res = new HashMap<>();
        JSONObject json = JSON.parseObject(phonenumberdata);
        Register register = new Register();
        try{
            String phonenumber=json.getString("phonenumber");
            String userid=json.getString("userid");
            return register.sendcodeChange(userid,phonenumber);
        }catch (JSONException e){
            res.put("msg","Wrong");
            e.printStackTrace();
            return JSON.toJSONString(res);
        }
    }

    //注册用户
    public static String register(String data){
        //phonenumber code password
        JSONObject json = new JSONObject();
        Register register = new Register();
        try {
            JSONObject map = JSON.parseObject(data);

            String phonenumber = map.getString("phonenumber");
            String code = map.getString("code");
            String password = map.getString("password");
            return register.register(phonenumber,code,password);
        }catch (Exception e){
            json.put("msg","Wrong");
            e.printStackTrace();
            return json.toString();
        }
    }

    //忘记密码时 修改密码
    public static String changePass(String data){
        //phonenumber code password
        JSONObject json = new JSONObject();
        Register register = new Register();
        try {
            JSONObject map = JSON.parseObject(data);

            String phonenumber = map.getString("phonenumber");
            String code = map.getString("code");
            String password = map.getString("password");
            return register.changePass(phonenumber,code,password);
        }catch (Exception e){
            json.put("msg","Wrong");
            e.printStackTrace();
            return json.toString();
        }
    }

    //修改绑定手机
    public static String changePhone(String data){
        //phonenumber code
        //phonenumber code userid
        JSONObject json = new JSONObject();
        Register register = new Register();
        try {
            JSONObject map = JSON.parseObject(data);
            String userid = map.getString("userid");
            String phonenumber = map.getString("phonenumber");
            String code = map.getString("code");
            return register.changePhone(userid,phonenumber,code);
        }catch (Exception e){
            json.put("msg","Wrong");
            e.printStackTrace();
            return json.toString();
        }

    }
    public static void main(String[] args) {
        /*
        //测试注册发送验证码
        String str="{'phonenumber':'13752275462'}";
        String coderesiger=codeRegister(str);
        System.out.println("测试注册时发送验证码："+coderesiger);
        */

        /*
        //测试注册
        String str="{\"phonenumber\":\"13752275462\",\"code\"=\"123223\",\"password\":\"123456'}";
        str="{'phonenumber':'13752275462','code':'123123','password':'123456'}";
        String resiger=register(str);
        System.out.println(resiger);

        */

        //测试忘记密码

        //测试修改手机号

    }


}
