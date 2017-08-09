package com.lovegod.newbuy.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/7/31.
 */

public class Quest implements Serializable {
    //问题的字段
    private int qid;
    private int cid;
    private int uid;
    private String username;
    private String title;
    private String asktime;
    private int up;
    private List<Reply> replies=new ArrayList<>();

    public class Reply implements Serializable{
        private int rid;
        private int qid;
        private int uid;
        private String username;
        private int buystatue;
        private int up;
        private String content;
        private String retime;

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public int getQid() {
            return qid;
        }

        public void setQid(int qid) {
            this.qid = qid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getBuystatue() {
            return buystatue;
        }

        public void setBuystatue(int buystatue) {
            this.buystatue = buystatue;
        }

        public int getUp() {
            return up;
        }

        public void setUp(int up) {
            this.up = up;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRetime() {
            return retime;
        }

        public void setRetime(String retime) {
            this.retime = retime;
        }
    }
    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAsktime() {
        return asktime;
    }

    public void setAsktime(String asktime) {
        this.asktime = asktime;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
