package all.callaction.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Addaction {

    public void addaction(Add addaction){
        //检验车牌号和手机号是否有效
        if(checkcarnumber(addaction.getCarnumber())&&checkphonenumber(addaction.getPhonenumber())){
            //添加报警信息

        }

        //检验定位信息



    }

    public boolean checkcarnumber(String cph){
        boolean isok=false;
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
        Matcher matcher = pattern.matcher(cph);
        if (!matcher.matches()) {
            System.out.println("车牌号格式不对！");
        }else{
            isok=true;
            System.out.println("车牌号格式正确！");
        }
        return isok;
    }

    public boolean checkphonenumber(String phonenumber){
        boolean str= true;
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phonenumber.length() != 11){
            str=false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phonenumber);
            boolean isMatch = m.matches();
            if(!isMatch){
                str=false;
            }
        }
        return str;
    }


}
