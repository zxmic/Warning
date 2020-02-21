package all.certificatio;
import all.util.JdbcUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class CertificatioDao {
    Connection conn;
    Statement st;
    ResultSet rs;

    public CertificatioDao(){
        conn=JdbcUtil.getConnection();
        try {
            st=conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean certificatioAdd(String name,String id,String username){
        try{
            String sql="insert into user(name,id,username)values('"+ name +"','"+id+"','"+ username+"') ";
            st.execute(sql);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean carUpdate(String name,String id,String username){

        try{
            //修改信息
            String sql = "update set name='"+name+"',username='"+username+"' where id='"+id+"'";
            st.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        JdbcUtil.close_2(conn,st,rs);
    }

}
