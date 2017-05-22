package cf.qishui.sodnotelean.database;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by wangxiao on 2017/5/22.
 */

@Table(database = SodLeanDB.class, allFields = true)
public class Notebook extends BaseModel {
    @PrimaryKey
    public String NotebookId;
    public String UserId;
    public String ParentNotebookId;
    public int Seq;
    public String Title;
    public boolean IsBlog;
    public boolean IsDeleted;
    public String CreatedTime;
    public String UpdatedTime;
    public int Usn;
}
