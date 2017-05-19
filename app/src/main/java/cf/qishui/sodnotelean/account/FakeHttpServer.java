package cf.qishui.sodnotelean.account;

/**
 * Created by wangxiao on 2017/5/17.
 */

public class FakeHttpServer {
    public String login(String name, String passwd) {
        return "welcome " + name;
    }
}
