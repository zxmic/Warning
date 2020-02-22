package all.register.other;

import all.login.other.User;
import all.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterDao {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    public RegisterDao(){
        conn= JdbcUtil.getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userAdd(Long userid,String phonenumber, String password){

        try{
            //添加用户信息
            String sql = "insert into user(userid,phonenumber,password) values('"+ userid +"','"+phonenumber+"','"+password+"')";
            stmt.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean userUpdatePass(String userid, String password){
        try{
            String sql = "update user set password= '"+password+"' WHERE userid='"+userid+"'";
            stmt.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
   }

    public boolean userUpdatePhone(String userid, String phone){
        try{
            String sql = "update user set phonenumber= '"+phone+"' WHERE userid='"+userid+"'";
            stmt.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean phoneUpdatePass(String phonenumber, String pass){
        try{
            String sql = "update user set password= '"+pass+"' WHERE phonenumber='"+phonenumber+"'";
            stmt.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public User userSelectByPhone(String phonenumber){

        User user=new User();
        String sql="select userid,phonenumber,password from user where phonenumber ='"+phonenumber+"'";
        try {
            rs=stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs.next()){
                user.setUserid(rs.getInt("userid"));
                user.setPhoneNumber(rs.getString("phonenumber"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("没有搜索到user信息");
            e.printStackTrace();
        }
        return null;
    }

    public User userSelect(String userid){

        User user=new User();
        String sql="select userid,phonenumber,password from user where userid ='"+userid+"'";
        try {
            rs=stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs.next()){
                user.setUserid(rs.getInt("userid"));
                user.setPhoneNumber(rs.getString("phonenumber"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("没有搜索到user信息");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        JdbcUtil.close_2(conn,stmt,rs);
    }

}
