package cf.qishui.sodnotelean.model;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by wangxiao on 2017/5/21.
 */

@Table(database = SodLeanDB.class, allFields = true)
public class LoginModel extends SodaBaseModel {
    @PrimaryKey
    public String UserId;
    public String Token;
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
