package com.myevent.domain;

import com.google.api.services.calendar.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class MyEvent extends GoogleEvent implements Cloneable {
    private String keyword;
    private String category1;
    private String category2;
    private String category3;
    private String requester;
    private String content;
    private String srId;
    private String subsystem;
    private Date createdDate;
    private Date modifiedDate;

    private int minutes;

    public MyEvent() {

    }

    public MyEvent(Event event) {
        super(event);

        // 내용 파싱
        String summary = StringUtils.replace(StringUtils.trim(event.getSummary()), "  ", " ");
        String header = StringUtils.substringBetween(summary, "[", "]");
        String body = StringUtils.remove(summary, "[" + header + "]");

        if ("ITRM".equalsIgnoreCase(StringUtils.trim(event.getSummary()))) {
            setKeyword("ITRM");
            setSrId("미발행");
            setSubsystem("E-Commerce");
            setContent(summary);
            setRequester("민선호");
            return;
        }

        header = StringUtils.trim(header);
        body = StringUtils.trim(body);
        String[] headers = StringUtils.splitByWholeSeparatorPreserveAllTokens(header, ";");

        setKeyword(header);
        if (headers != null) {
            setKeyword(headers.length > 0 ? StringUtils.trim(headers[0]) : header);
            setSrId(headers.length > 1 ? StringUtils.trim(headers[1]) : null);
            setSubsystem(headers.length > 2 ? StringUtils.trim(headers[2]) : null);
        }

        setContent(body);
        setRequester("민선호");
        if (body.indexOf("-") > 0) {
            String body1 = StringUtils.trim(StringUtils.substringBefore(body, "-"));
            String body2 = StringUtils.trim(StringUtils.substringAfter(body, "-"));

            if (body1.indexOf(" ") == -1) {
                setContent(body2);
                setRequester(body1);
            }
        }
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void calculateMinutes() {
        this.minutes = Math.round((getEnd().getTime() - getStart().getTime()) / 1000 / 60);
    }

    public Object clone() throws CloneNotSupportedException {
        MyEvent a = (MyEvent)super.clone();
        return a;
    }
}