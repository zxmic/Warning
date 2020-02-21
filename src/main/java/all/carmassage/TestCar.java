package all.carmassage;

import all.carmassage.other.Token;

public class TestCar {

    public static String token(String data){
        Token t = new Token();
        return t.getToken(data);
    }
    public static void main(String[] args) {

        String str="{'cph':'WJ6344'}";
        token(str);

    }
}
