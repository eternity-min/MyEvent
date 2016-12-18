package com.myevent.domain;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.util.Date;

/**
 * Created by hope on 2016. 12. 17..
 */
public class GoogleEvent {
    private String id;
    private String iCalUid;
    private String etag;

    private Date start;
    private Date end;
    private String kind;
    private String creatorEmail;
    private String summary;
    private String description;
    private String location;
    private String htmlLink;
    private Date created;
    private Date updated;

    public GoogleEvent() {

    }

    public GoogleEvent(Event event) {
        this.id = event.getId();
        this.iCalUid = event.getICalUID();
        this.etag = event.getEtag();
        this.kind = event.getKind();
        this.creatorEmail = event.getCreator().getEmail();
        this.summary = event.getSummary();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.htmlLink = event.getHtmlLink();
        this.created = new Date(event.getCreated().getValue());
        this.updated = new Date(event.getUpdated().getValue());

        DateTime startDateTime = event.getStart().getDateTime();
        if (startDateTime == null) {
            startDateTime = event.getStart().getDate();
        }

        DateTime endDateTime = event.getEnd().getDateTime();
        if (endDateTime == null) {
            endDateTime = event.getEnd().getDate();
        }

        start = new Date(startDateTime.getValue());
        end = new Date(endDateTime.getValue());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getiCalUid() {
        return iCalUid;
    }

    public void setiCalUid(String iCalUid) {
        this.iCalUid = iCalUid;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
