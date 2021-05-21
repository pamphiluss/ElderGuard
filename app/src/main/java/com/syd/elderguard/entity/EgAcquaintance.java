package com.syd.elderguard.entity;

import java.io.Serializable;

/**
 * eg_acquaintance
 *
 * @author
 */
public class EgAcquaintance implements Serializable {
    private Integer acid;

    private String name;

    private String nickname;

    private String gender;

    private String phone;

    private String mail;

    private String image;

    private Integer relationshipid;

    private String update;

    private String crdate;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    private Integer userid;

    public Integer getAcid() {
        return acid;
    }

    public void setAcid(Integer acid) {
        this.acid = acid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getRelationshipid() {
        return relationshipid;
    }

    public void setRelationshipid(Integer relationshipid) {
        this.relationshipid = relationshipid;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getCrdate() {
        return crdate;
    }

    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }
}