package com.xuexiang.elderguard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * eg_relationship
 * @author 
 */
public class EgRelationship implements Serializable {
    private Integer relationid;

    private String name;

    private Date update;

    private Date crdate;

    private Integer userid;

    private static final long serialVersionUID = 1L;

    public Integer getRelationid() {
        return relationid;
    }

    public void setRelationid(Integer relationid) {
        this.relationid = relationid;
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
        EgRelationship other = (EgRelationship) that;
        return (this.getRelationid() == null ? other.getRelationid() == null : this.getRelationid().equals(other.getRelationid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getUpdate() == null ? other.getUpdate() == null : this.getUpdate().equals(other.getUpdate()))
            && (this.getCrdate() == null ? other.getCrdate() == null : this.getCrdate().equals(other.getCrdate()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRelationid() == null) ? 0 : getRelationid().hashCode());
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
        sb.append(", relationid=").append(relationid);
        sb.append(", name=").append(name);
        sb.append(", update=").append(update);
        sb.append(", crdate=").append(crdate);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}