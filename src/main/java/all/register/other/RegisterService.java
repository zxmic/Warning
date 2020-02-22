package all.register.other;


import all.login.other.User;

public class RegisterService {
    private RegisterDao registerDao=new RegisterDao();

    public boolean userAdd(Long userid,String phonenumber, String password){
        boolean flag=false;
        boolean isAdd=registerDao.userAdd(userid,phonenumber,password);
        if(isAdd){
            flag=true;
            return flag;
        }
        return flag;
    }

    //修改绑定手机号
    public boolean userUpdatePhone(String userid, String phonenumber){
        User user=userSelect(userid);
        boolean flag=false;
        if(user!=null){
            flag=registerDao.userUpdatePhone(userid,phonenumber);
        }
        return flag;

    }

    public boolean phoneUpdatePass(String phonenumber, String pass){
        User user = registerDao.userSelectByPhone(phonenumber);
        boolean flag=false;
        if(user!=null){
            flag=registerDao.phoneUpdatePass(phonenumber,pass);
        }
        return flag;
    }

    public User userSelect(String userid){
        User user=registerDao.userSelect(userid);
        if(user!=null){
            return user;
        } else{
            return null;
        }
    }

}
