package all.login.other;

import java.io.Serializable;

public class User implements Serializable {
    private int userid;
    private String phoneNumber="phoneNumber";
    private String password="password";

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User() {
    }

    public void setUserid(int id) {
        this.userid = id;
    }

    public int getUserid() {
        return userid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
