package all.callaction.other;

import all.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionDao {
    Connection conn;
    Statement stmt;
    ResultSet rs;

    public ActionDao(){
        conn= JdbcUtil.getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getnowtime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time=df.format(new Date());
        System.out.println(time);// new Date()为获取当前系统时间
        return time;
    }

    public boolean addaction(Add action){

        String calltime=getnowtime();
        String overtiome="no";
        String state="1";
        try{
            //添加用户信息
            String sql = "insert into action (actionid,userid,carid,calltime,overtime,callplace," +
                    "describes,state,actionusername,carnumber,picture) values('"
                    + action.getActionid() +"','"+action.getUserid()+"','"+action.getCarid()+"','"
                    +calltime+"','"+overtiome+"','"+action.getPlace()+"','"+action.getDescription()+"','"
                    +state+"','"+action.getName()+"','"+action.getCarnumber()+"','"+action.getPicture()+"')";
            stmt.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        JdbcUtil.close_2(conn,stmt,rs);
    }

}
