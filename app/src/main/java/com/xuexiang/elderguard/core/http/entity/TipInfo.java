package com.xuexiang.elderguard.core.http.entity;

import androidx.annotation.Keep;

/**
 * @author xuexiang
 * @since 2019-08-28 15:35
 */
@Keep
public class TipInfo {


    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TipInfo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
