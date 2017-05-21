package cf.qishui.sodnotelean.model;

/**
 * Created by wangxiao on 2017/5/21.
 */

public class LoginModel {
    public boolean Ok;
    public String Token;
    public String UserId;
    public String Email;
    public String Username;

    @Override
    public String toString() {
        return "LoginModel{" +
                "Ok=" + Ok +
                ", Token='" + Token + '\'' +
                ", UserId='" + UserId + '\'' +
                ", Email='" + Email + '\'' +
                ", Username='" + Username + '\'' +
                '}';
    }
}
