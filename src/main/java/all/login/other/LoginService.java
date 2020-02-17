package all.login.other;

public class LoginService {

    LoginDao loginDao=new LoginDao();

    public LoginService(){

    }

    public User selectUserByPhoneNumber(String userid){
        User user = loginDao.selectUserByP(userid);
        //数据库没有搜索到用户信息 ==》提示注册
        if(user!=null){
            return user;
        }
        return null;
    }




}
