package all.util;

/**
 * @ClassName TokenUtil
 * @Description 获取token
 * @Author Liyihe
 * @Date 2020/01/07 下午10:28
 * @Version 1.0
 */
public class TokenUtil {
    public static String getToken(String userid){
        return userid+System.currentTimeMillis();
    }
}
