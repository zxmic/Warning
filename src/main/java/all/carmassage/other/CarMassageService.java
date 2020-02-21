package all.carmassage.other;


public class CarMassageService {
    private CarMassageDao carMassageDao=new CarMassageDao();

    public boolean carAdd(String 类型,String 颜色,String 型号,String cph,String 乘员数,String 制造年月,String 品牌){
        boolean flag=false;
        boolean isAdd=carMassageDao.carAdd(类型,颜色,型号,cph,乘员数,制造年月,品牌);
        if(isAdd){
            flag=true;
            return flag;
        }
        return flag;
    }

    public boolean carUpdate(String 类型,String 颜色,String 型号,String cph,String 乘员数,String 制造年月,String 品牌){
        Car car=carSelect(cph);
        boolean flag=false;
        if(car!=null){
            flag=carMassageDao.carUpdate(类型,颜色,型号,cph,乘员数,制造年月,品牌);
        }
        return flag;

    }

    public Car carSelect(String cph){
        Car car=carMassageDao.carSelect(cph);
        if(car!=null){
            return car;
        } else{
            return null;
        }
    }
}
