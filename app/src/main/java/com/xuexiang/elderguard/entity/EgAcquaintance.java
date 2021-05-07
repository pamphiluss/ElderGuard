package com.xuexiang.elderguard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * eg_acquaintance
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

    private Date update;

    private Date crdate;

    private Integer userid;

    private static final long serialVersionUID = 1L;

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

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public Date getCrdate() {
        return crdate;
    }

    public void setCrdate(Date crdate) {
        this.crdate = crdate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EgAcquaintance other = (EgAcquaintance) that;
        return (this.getAcid() == null ? other.getAcid() == null : this.getAcid().equals(other.getAcid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getMail() == null ? other.getMail() == null : this.getMail().equals(other.getMail()))
            && (this.getImage() == null ? other.getImage() == null : this.getImage().equals(other.getImage()))
            && (this.getRelationshipid() == null ? other.getRelationshipid() == null : this.getRelationshipid().equals(other.getRelationshipid()))
            && (this.getUpdate() == null ? other.getUpdate() == null : this.getUpdate().equals(other.getUpdate()))
            && (this.getCrdate() == null ? other.getCrdate() == null : this.getCrdate().equals(other.getCrdate()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAcid() == null) ? 0 : getAcid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getMail() == null) ? 0 : getMail().hashCode());
        result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
        result = prime * result + ((getRelationshipid() == null) ? 0 : getRelationshipid().hashCode());
        result = prime * result + ((getUpdate() == null) ? 0 : getUpdate().hashCode());
        result = prime * result + ((getCrdate() == null) ? 0 : getCrdate().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", acid=").append(acid);
        sb.append(", name=").append(name);
        sb.append(", nickname=").append(nickname);
        sb.append(", gender=").append(gender);
        sb.append(", phone=").append(phone);
        sb.append(", mail=").append(mail);
        sb.append(", image=").append(image);
        sb.append(", relationshipid=").append(relationshipid);
        sb.append(", update=").append(update);
        sb.append(", crdate=").append(crdate);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}