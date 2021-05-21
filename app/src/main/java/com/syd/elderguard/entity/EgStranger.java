package com.syd.elderguard.entity;

import java.io.Serializable;

/**
 * eg_stranger
 *
 * @author
 */
public class EgStranger implements Serializable {
    private Integer strangerid;

    private String image;

    private String strangername;


    private String crdate;

    private Integer userid;

    public Integer getStrangerid() {
        return strangerid;
    }

    public void setStrangerid(Integer strangerid) {
        this.strangerid = strangerid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStrangername() {
        return strangername;
    }

    public void setStrangername(String strangername) {
        this.strangername = strangername;
    }

    public String getCrdate() {
        return crdate;
    }

    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}