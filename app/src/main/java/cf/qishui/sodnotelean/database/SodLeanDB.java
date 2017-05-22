package cf.qishui.sodnotelean.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by wangxiao on 2017/5/20.
 */

@Database(name = SodLeanDB.NAME, version = SodLeanDB.VERSION)
public class SodLeanDB {
    public static final String NAME = "sod_lean_db";
    public static final int VERSION = 1;
}
