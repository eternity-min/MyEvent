package com.myevent.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.myevent.domain.GoogleEvent;
import com.myevent.domain.MyEvent;
import com.myevent.service.MyEventService;
import com.myevent.util.GoogleCalendarUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hope on 2016. 12. 16..
 */
@Controller
@RequestMapping("/calendar")
public class MyEventController {

    @Autowired
    private MyEventService myEventService;

    @RequestMapping("/event/local/list")
    public String listLocalEvents(Model model, String searchStart, String searchEnd) throws Exception {
        String[] datePattern = new String[]{"yyyyMMddhhmmss", "yyyy-MM-dd hh:mm:ss", "yyyyMMdd", "yyyy-MM-dd"};
        Date start = StringUtils.isBlank(searchStart) ? new Date() : DateUtils.parseDate(searchStart, datePattern);
        Date end = StringUtils.isBlank(searchEnd) ? new Date() : DateUtils.parseDate(searchEnd, datePattern);

        List<MyEvent> myEvents = myEventService.listMyEventWithMinutes(start, end);

        model.addAttribute("myEvents", myEvents);
        return "calendar/event/listLocal";
    }

    @RequestMapping("/event/google/list")
    public String listGoogleEvents(Model model, String searchStart, String searchEnd) throws IOException, ParseException {
        String[] datePattern = new String[]{"yyyyMMddhhmmss", "yyyy-MM-dd hh:mm:ss", "yyyyMMdd", "yyyy-MM-dd"};
        Date start = StringUtils.isBlank(searchStart) ? new Date() : DateUtils.parseDate(searchStart, datePattern);
        Date end = StringUtils.isBlank(searchEnd) ? new Date() : DateUtils.parseDate(searchEnd, datePattern);

        List<MyEvent> myEvents = myEventService.listEventsFromGoogleCalendar(start, end);

        for(MyEvent item : myEvents) {
            MyEvent myEvent = myEventService.findMyEventById(item.getId());
            if(myEvent != null) {
                item.setCreatedDate(myEvent.getCreatedDate());
                item.setModifiedDate(myEvent.getModifiedDate());
            }
        }

        model.addAttribute("myEvents", myEvents);
        model.addAttribute("category1s", myEventService.listCategoryByLevel(1));
        model.addAttribute("category2s", myEventService.listCategoryByLevel(2));
        model.addAttribute("category3s", myEventService.listCategoryByLevel(3));
        return "calendar/event/listGoogle";
    }

    @RequestMapping("/event/local/save")
    public String saveGoogleEventsToLocal(Model model, String searchStart, String searchEnd, String[] checkedIds) throws IOException, ParseException {
        String[] datePattern = new String[]{"yyyyMMddhhmmss", "yyyy-MM-dd hh:mm:ss", "yyyyMMdd", "yyyy-MM-dd"};
        Date start = DateUtils.parseDate(searchStart, datePattern);
        Date end = DateUtils.parseDate(searchEnd, datePattern);
        myEventService.saveGoogleEventsToMyCalendar(start, end, checkedIds);

        return "redirect:/calendar/event/google/list?searchStart=" + searchStart + "&searchEnd=" + searchEnd;
    }

    @ResponseBody
    @RequestMapping(value = "/event/google/updateSummary", headers = "Accept=application/json")
    public MyEvent updateSummaryForGoogleEvent(String id, String summary) throws IOException {
        return myEventService.convertToMyEvent(GoogleCalendarUtils.updateSummary(id, summary));
    }

}
