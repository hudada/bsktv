package com.example.bsproperty.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongBean implements Serializable {

    private Long id;
    private String song_name;
    private Long userid;
    private int time;
    private ArrayList<ReplyBean> replyBeans;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<ReplyBean> getReplyBeans() {
        return replyBeans;
    }

    public void setReplyBeans(ArrayList<ReplyBean> replyBeans) {
        this.replyBeans = replyBeans;
    }
}
