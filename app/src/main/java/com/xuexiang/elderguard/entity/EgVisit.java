package com.xuexiang.elderguard.entity;

/**
 * eg_visit
 *
 * @author
 */
public class EgVisit {
    private Integer visitid;

    private String image;

    private String visitName;

    private String crdate;

    private Integer userid;


    public Integer getVisitid() {
        return visitid;
    }

    public void setVisitid(Integer visitid) {
        this.visitid = visitid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVisitName() {
        return visitName;
    }

    public void setVisitName(String visitName) {
        this.visitName = visitName;
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