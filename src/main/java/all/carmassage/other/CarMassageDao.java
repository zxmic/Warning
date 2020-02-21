package all.carmassage.other;

import all.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarMassageDao {

    Connection conn;
    Statement st;
    ResultSet rs;

    public CarMassageDao(){
        conn= JdbcUtil.getConnection();
        try {
            st=conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean carAdd(String 类型,String 颜色,String 型号,String 车牌号,String 乘员数,String 制造年月,String 品牌){

        try{
            //添加车辆信息
            String sql = "insert into user(类型,颜色,型号,车牌号,乘员数,制造年月,品牌) values('"+ 类型 +"','"+颜色+"','"+型号+"','"+车牌号+"','"+乘员数+"','"+制造年月+"','"+品牌+"')";
            st.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean carUpdate(String 类型,String 颜色,String 型号,String cph,String 乘员数,String 制造年月,String 品牌){

        try{
            //修改车辆信息
            String sql = "update set 类型='"+类型+"',颜色='"+颜色+"',型号='"+型号+"',乘员数='"+乘员数+"',制造年月='"+制造年月+"'品牌='"+品牌+"' where 车牌号='"+cph+"'";
            st.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


public Car carSelect(String 车牌号){

    Car car=new Car();
    String sql="select 类型,颜色,型号,乘员数,制造年月,品牌 from user where 车牌号 ='"+车牌号+"'";
    try {
        rs=st.executeQuery(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    try {
        if(rs.next()){
            car.set类型(rs.getString("轿车"));
            car.set颜色(rs.getString("白色"));
            car.set型号(rs.getString("xxx"));
            car.setCph(rs.getString("cph"));
            car.set乘员数(rs.getString("5人"));
            car.set制造年月(rs.getString("2010-08"));
            car.set品牌(rs.getString("奥迪"));
            return car;
        }
    } catch (SQLException e) {
        System.out.println("没有搜索到car信息");
        e.printStackTrace();
    }
    return null;
}

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        JdbcUtil.close_2(conn,st,rs);
    }

}
