package all.carmassage.other;

public class TokenE {
    //token
    private String cph="cph";
    private String token="token";
    //提示信息
    private String msg="msg";

    public TokenE() {
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCph(){
        return cph;
    }

    public void setCph(String cph){
        this.cph=cph;
    }



    @Override
    public String toString() {
        return "TokenE{" +
                "cph='" + cph + '\'' +
                ", token='" + token + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
