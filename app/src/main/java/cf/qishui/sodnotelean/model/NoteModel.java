package cf.qishui.sodnotelean.model;

import java.util.List;

/**
 * Created by wangxiao on 2017/5/23.
 */

public class NoteModel {
    public String NoteId;
    public String NotebookId;
    public String UserId;
    public String Title;
    public String Desc;
    public String Abstract;
    public String Content;
    public boolean IsMarkdown;
    public boolean IsBlog;
    public boolean IsTrash;
    public boolean IsDeleted;
    public int Usn;
    public String CreatedTime;
    public String UpdatedTime;
    public String PublicTime;
    public List<String> Tags;
    public List<String> Files;
}
