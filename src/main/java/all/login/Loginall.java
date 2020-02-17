package all.login;

import all.login.other.Login;
import all.login.other.Token;

public class Loginall {

    public String login(String data){
        Login l = new Login();
        return l.loginM(data);
    }

    public String token(String data){
         Token t = new Token();
         return t.getToken(data);
    }

}
