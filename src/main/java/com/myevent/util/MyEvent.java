package com.myevent.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.util.Date;

/**
 * Created by hope on 2016. 11. 18..
 * <p>
 * [업무구분] 요청/담당자 - 업무내용 - 업무세부 내용
 * [SR - SR번호] 요청자 - [SR요청구분] SR 제목
 */
public class MyEvent {
    public static String DEFAULT_SUBSYSTEM = "E-Commerce";

    private Connection connection;
    private Event event;

    private Date start;
    private Date end;
    private int minutes;

    private String keyword;
    private String srId;
    private String subsystem;
    private String content;
    private String requester;

    private String category1;
    private String category2;
    private String category3;

    public MyEvent(Event event, Connection connection) {
        this.event = event;
        this.connection = connection;

        parse();
        setTimes();
    }

    public void setTimes() {
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
        minutes = Math.round((endDateTime.getValue() - startDateTime.getValue()) / 1000 / 60);
    }

    /**
     * Summary 패턴 : [구분] 요청자 - 내용
     * - 구분 :
     * 대분류 - 중분류 - 소분류 ; SR(default = 미발행 ; 서브시스템(default = E-Commerce)
     * 업무회의 => 대분류...
     * 문의응대 => ....
     * SR - C16110002561 =>
     * - 간단 패턴 예
     * [업무회의] 온라인/MDM 마케팅 수신동의 불일치 이슈 분석
     * [업무회의] 알리페이 정산 관련
     * ITRM => ...
     */
    public void parse() {
        String summary = StringUtils.replace(StringUtils.trim(event.getSummary()), "  ", " ");

        if ("ITRM".equalsIgnoreCase(summary)) {
            this.keyword = "ITRM";
            this.srId = "";
            this.subsystem = "E-Commerce";
            this.content = summary;
            this.requester = "민선호";
            return;
        }

        String header = StringUtils.substringBetween(summary, "[", "]");
        String body = StringUtils.remove(summary, "[" + header + "]");

        header = StringUtils.trim(header);
        body = StringUtils.trim(body);
        String[] headers = StringUtils.splitByWholeSeparatorPreserveAllTokens(header, ";");

        this.keyword =  header;
        if(headers != null) {
            this.keyword = headers.length > 0 ? StringUtils.trim(headers[0]) : header;
            this.srId = headers.length > 1 ? StringUtils.trim(headers[1]) : "";
            this.subsystem = headers.length > 2 ? StringUtils.trim(headers[2]) : "";
        }

        this.content = body;
        this.requester = "민선호";

        String body1 = "", body2 = "";
        if (body.indexOf("-") > 0) {
            body1 = StringUtils.trim(StringUtils.substringBefore(body, "-"));
            body2 = StringUtils.trim(StringUtils.substringAfter(body, "-"));

            if (body1.indexOf(" ") == -1) {
                this.content = body2;
                this.requester = body1;
            }
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
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
}
