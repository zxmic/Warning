package all.login.other;

public class TokenE {
    //token
    private String userid="userid";
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "TokenE{" +
                "userid='" + userid + '\'' +
                ", token='" + token + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
