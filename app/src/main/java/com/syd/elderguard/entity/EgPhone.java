package com.syd.elderguard.entity;

import java.io.Serializable;

/**
 * eg_phone
 * @author 
 */
public class EgPhone implements Serializable {
    private Integer phoneid;

    private String phone;

    private Integer userid;

    private static final long serialVersionUID = 1L;

    public Integer getPhoneid() {
        return phoneid;
    }

    public void setPhoneid(Integer phoneid) {
        this.phoneid = phoneid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        EgPhone other = (EgPhone) that;
        return (this.getPhoneid() == null ? other.getPhoneid() == null : this.getPhoneid().equals(other.getPhoneid()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPhoneid() == null) ? 0 : getPhoneid().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", phoneid=").append(phoneid);
        sb.append(", phone=").append(phone);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}