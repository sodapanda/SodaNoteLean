package cf.qishui.sodnotelean.database;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by wangxiao on 2017/5/22.
 */

@Table(database = SodLeanDB.class, allFields = true)
public class UserInfoTable extends BaseModel {
    @PrimaryKey
    public String UserId;
    public String Token;
    public String Email;
    public String Username;
    public String Avatar;
    public boolean Verified;
}
