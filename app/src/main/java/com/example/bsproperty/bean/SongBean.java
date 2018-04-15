package com.example.bsproperty.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongBean implements Serializable {

    private Long id;
    private Long uid;
    private String name;
    private String addr;
    private String addrBack;
    private Long length;
    private int likeSum;  //赞
    private String uname;
    private boolean isLike;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public int getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(int likeSum) {
        this.likeSum = likeSum;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getAddrBack() {
        return addrBack;
    }

    public void setAddrBack(String addrBack) {
        this.addrBack = addrBack;
    }
}
