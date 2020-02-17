package all.login.other;

import all.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginDao {
    Connection conn;
    Statement stmt;
    ResultSet rs;

    public LoginDao(){
        conn= JdbcUtil.getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据电话号码 查询用户名密码
    public User selectUserByP(String userid){

        User user=new User();
        String sql="select userid,phonenumber,password from user where userid ='"+userid+"'";
        try {
            rs=stmt.executeQuery(sql);
        } catch (SQLException e) {
            //System.out.println("selectUserByP");
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
