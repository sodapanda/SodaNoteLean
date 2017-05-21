package cf.qishui.sodnotelean.model;

import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by wangxiao on 2017/5/21.
 */

public class SodaBaseModel extends BaseModel {
    public boolean Ok = true;
    public String Msg;

    @Override
    public String toString() {
        return "SodaBaseModel{" +
                "Ok=" + Ok +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
