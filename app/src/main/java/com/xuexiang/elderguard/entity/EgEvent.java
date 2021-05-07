package com.xuexiang.elderguard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * eg_event
 * @author 
 */
public class EgEvent implements Serializable {
    private Integer eventid;

    private String name;

    private Date update;

    private Date crdate;

    private Integer userid;

    private static final long serialVersionUID = 1L;

    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        EgEvent other = (EgEvent) that;
        return (this.getEventid() == null ? other.getEventid() == null : this.getEventid().equals(other.getEventid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getUpdate() == null ? other.getUpdate() == null : this.getUpdate().equals(other.getUpdate()))
            && (this.getCrdate() == null ? other.getCrdate() == null : this.getCrdate().equals(other.getCrdate()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEventid() == null) ? 0 : getEventid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
        sb.append(", eventid=").append(eventid);
        sb.append(", name=").append(name);
        sb.append(", update=").append(update);
        sb.append(", crdate=").append(crdate);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}