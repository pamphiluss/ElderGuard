package com.xuexiang.elderguard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * eg_stranger
 * @author 
 */
public class EgStranger implements Serializable {
    private Integer strangerid;

    private String image;

    private Integer time;

    private Date update;

    private Date crdate;

    private Integer userid;

    private static final long serialVersionUID = 1L;

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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
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
        EgStranger other = (EgStranger) that;
        return (this.getStrangerid() == null ? other.getStrangerid() == null : this.getStrangerid().equals(other.getStrangerid()))
            && (this.getImage() == null ? other.getImage() == null : this.getImage().equals(other.getImage()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
            && (this.getUpdate() == null ? other.getUpdate() == null : this.getUpdate().equals(other.getUpdate()))
            && (this.getCrdate() == null ? other.getCrdate() == null : this.getCrdate().equals(other.getCrdate()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStrangerid() == null) ? 0 : getStrangerid().hashCode());
        result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
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
        sb.append(", strangerid=").append(strangerid);
        sb.append(", image=").append(image);
        sb.append(", time=").append(time);
        sb.append(", update=").append(update);
        sb.append(", crdate=").append(crdate);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}