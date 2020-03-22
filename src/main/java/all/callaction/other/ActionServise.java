package all.callaction.other;

public class ActionServise {
    ActionDao actionDao=new ActionDao();

    public boolean addaction(Add action){
        boolean isadd=actionDao.addaction(action);
        return isadd;
    }

}
