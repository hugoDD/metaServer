package cn.granitech.integration.dingtalk;

import java.util.List;

public class UserPage {
    private boolean has_more;
    private List<User> list;
    private int next_cursor;

    public boolean isHas_more() {
        return this.has_more;
    }

    public void setHas_more(boolean has_more2) {
        this.has_more = has_more2;
    }

    public int getNext_cursor() {
        return this.next_cursor;
    }

    public void setNext_cursor(int next_cursor2) {
        this.next_cursor = next_cursor2;
    }

    public List<User> getList() {
        return this.list;
    }

    public void setList(List<User> list2) {
        this.list = list2;
    }
}
