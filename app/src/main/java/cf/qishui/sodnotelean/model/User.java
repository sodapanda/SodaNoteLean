package cf.qishui.sodnotelean.model;

/**
 * Created by wangxiao on 2017/5/21.
 */

public class User extends SodaBaseModel {
    public String UserId;
    public String Username;
    public String Email;
    public boolean Verified;
    public String Logo;

    @Override
    public String toString() {
        return "User{" +
                "UserId='" + UserId + '\'' +
                ", Username='" + Username + '\'' +
                ", Email='" + Email + '\'' +
                ", Verified=" + Verified +
                ", Logo='" + Logo + '\'' +
                '}' +
                super.toString();
    }
}
