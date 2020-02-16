package all.util;


public class TokenUtil {
    public static String getToken(String userid){
        return userid+System.currentTimeMillis();
    }
}
