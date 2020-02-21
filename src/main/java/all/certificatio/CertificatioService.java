package all.certificatio;

public class CertificatioService {
    private CertificatioDao CertificatioDao=new CertificatioDao();

    public boolean PersonAdd(String id,String name ,String username){
        boolean flag=false;
        boolean isAdd=CertificatioDao.certificatioAdd(name ,id,username);
        if(isAdd){
            flag=true;
            return flag;
        }
        return flag;
    }

//    public boolean carUpdate(String name,String id,String username){
//        Person person=carSelect(id);
//        boolean flag=false;
//        if(person!=null){
//            flag=CertificatioDao.carUpdate(name,id,username);
//        }
//        return flag;
//
//    }
//
//    public Person carSelect(String cph){
//        Person person=CertificatioDao.carSelect(cph);
//        if(person!=null){
//            return person;
//        } else{
//            return null;
//        }
//    }
}
