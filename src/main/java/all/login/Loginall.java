package all.login;

import all.login.other.Login;
import all.login.other.Token;

public class Loginall {

    public static String login(String data){
        Login l = new Login();
        return l.loginM(data);
    }

    public static String token(String data){
         Token t = new Token();
         return t.getToken(data);
    }



    public static void main(String[] args) {




        /*
        //测试登录
        String str="{'phonenumber':'13752275462','password':'123456'}";
        login(str);
         */
    }

}
