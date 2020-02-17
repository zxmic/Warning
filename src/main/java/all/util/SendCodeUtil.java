package all.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.Random;

public class SendCodeUtil {
    private static final String regionId = "cn-hangzhou";
    private static final String accessKeyId = "LTAI4FrtpF3XXaYeQ895kMfF";
    private static final String secret = "6U5E3KwPJaNT0axCTVliavvvLwzySX";
    private static final DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
    private static final String domain = "dysmsapi.aliyuncs.com";
    private static final String action = "SendSms";
    private static final String version = "2017-05-25";
    private static final String RegionId = "RegionId";
    private static final String PhoneNumbers = "PhoneNumbers";
    private static final String SignName = "SignName";
    private static final String SignName_V = "交通事故自救报警App";
    private static final String TemplateCode = "TemplateCode";
    private static final String TemplateCode_V = "SMS_183765981";
    private static final String TemplateParam = "TemplateParam";
    private static final Random random = new Random();
    private static final int random_max = 88888;
    private static final int random_min = 11111;

    /*
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        sendCode("13752275462");
        System.out.println(System.currentTimeMillis() - start);
    }
    */
    public static String sendCode(String phonenumber) {
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setAction(action);
        request.setVersion(version);
        request.putQueryParameter(RegionId, regionId);
        request.putQueryParameter(PhoneNumbers, phonenumber);
        request.putQueryParameter(SignName, SignName_V);
        request.putQueryParameter(TemplateCode, TemplateCode_V);
        String code = String.valueOf(random.nextInt(random_max) + random_min);
        request.putQueryParameter(TemplateParam, "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
}