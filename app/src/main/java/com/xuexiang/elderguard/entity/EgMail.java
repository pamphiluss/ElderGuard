package com.xuexiang.elderguard.entity;

import java.io.Serializable;

/**
 * eg_mail
 * @author 
 */
public class EgMail implements Serializable {
    private Integer mailid;

    private String mail;

    private Integer userid;

    private static final long serialVersionUID = 1L;

    public Integer getMailid() {
        return mailid;
    }

    public void setMailid(Integer mailid) {
        this.mailid = mailid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
        EgMail other = (EgMail) that;
        return (this.getMailid() == null ? other.getMailid() == null : this.getMailid().equals(other.getMailid()))
            && (this.getMail() == null ? other.getMail() == null : this.getMail().equals(other.getMail()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMailid() == null) ? 0 : getMailid().hashCode());
        result = prime * result + ((getMail() == null) ? 0 : getMail().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mailid=").append(mailid);
        sb.append(", mail=").append(mail);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}