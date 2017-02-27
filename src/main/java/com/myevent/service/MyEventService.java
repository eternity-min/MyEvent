package com.myevent.service;

import com.google.api.services.calendar.model.Event;
import com.myevent.dao.MyEventMapper;
import com.myevent.domain.Category;
import com.myevent.domain.MyEvent;
import com.myevent.util.GoogleCalendarUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MyEventService {

    @Autowired
    private MyEventMapper myEventMapper;

    private static final String[] REQUIRED_SUBSYSTEM_CATEGORY1 = new String[]{"운영관리", "SR관리"};

    public List<MyEvent> listEventsFromGoogleCalendar(Date start, Date end) throws IOException {
        List<MyEvent> list = new ArrayList<MyEvent>();
        List<Event> items = GoogleCalendarUtils.listEvent(start, end);

        for (Event item : items) {
//            if (item.getId().equals("0s5ukr3f7g0qev1s1rnnt63n1o")) {
//                continue;
//            }

            list.add(convertToMyEvent(item));
        }

        return list;
    }

    public MyEvent convertToMyEvent(Event event) {
        MyEvent myEvent = new MyEvent(event);
        MyEvent category = findCategory(myEvent.getKeyword());

        if (category != null) {
            myEvent.setCategory1(category.getCategory1());
            myEvent.setCategory2(category.getCategory2());
            myEvent.setCategory3(category.getCategory3());

            myEvent.setContent(StringUtils.defaultIfBlank(myEvent.getContent(), "ITRM".equals(myEvent.getKeyword()) ? myEvent.getKeyword() : myEvent.getCategory3()));
            if(ArrayUtils.contains(REQUIRED_SUBSYSTEM_CATEGORY1, myEvent.getCategory1())) {
                myEvent.setSubsystem(StringUtils.defaultIfBlank(myEvent.getSubsystem(), "인터넷면세점(e-Commerce)"));
                myEvent.setSrId(StringUtils.defaultIfBlank(myEvent.getSrId(), "미발행"));
            }

        }

        return myEvent;
    }

    public MyEvent findCategory(String keyword) {
        return myEventMapper.findCategory(keyword);
    }

    public MyEvent findMyEventById(String id) {
        return myEventMapper.findMyEventById(id);
    }

    public List<Category> listCategoryByLevel(int level) {
        return myEventMapper.listCategoryByLevel(level);
    }

    public void saveGoogleEventsToMyCalendar(Date start, Date end, String[] checkedIds) throws IOException {
        List<MyEvent> myEvents = listEventsFromGoogleCalendar(start, end);
        for(MyEvent item : myEvents) {
            if(!StringUtils.isBlank(item.getCategory1())) {
                saveEvent(item);
            }
//            if(ArrayUtils.contains(checkedIds, item.getId())) {
//                saveEvent(item);
//            }
//            else {
//                //removeEventById(item.getId());
//            }
        }
    }

    public void saveEvent(MyEvent event) {
        myEventMapper.saveMyEvent(event);
        myEventMapper.saveGoogleEvent(event);
    }

    public void removeEventById(String id) {
        myEventMapper.removeMyEventById(id);
        myEventMapper.removeGoogleEventById(id);
    }

    public List<MyEvent> listMyEvent(Date start, Date end) {
        MyEvent param = new MyEvent();
        param.setStart(start);
        param.setEnd(end);
        return myEventMapper.listMyEventWithGoogleEvent(param);
    }

    public List<MyEvent> listMyEventWithMinutes(Date start, Date end) {
        List<MyEvent> myEvents = listMyEvent(start, end);
        List<MyEvent> list = new ArrayList<MyEvent>();
        for(MyEvent item : myEvents) {
            Date startDate = DateUtils.truncate(item.getStart(), Calendar.DATE);
            Date endDate = DateUtils.truncate(item.getEnd(), Calendar.DATE);
            int day = Math.round((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24);

            if(day == 0) {
                item.calculateMinutes();
                list.add(item);
                continue;
            }

            for(int i = 0 ; i < day; i++) {
                MyEvent event = ObjectUtils.clone(item);
                event.setStart(i == 0 ? item.getStart() : DateUtils.truncate(DateUtils.addDays(item.getStart(), i), Calendar.DATE));
                event.setEnd(i == day - 1 ? item.getEnd() : DateUtils.truncate(DateUtils.addDays(event.getStart(), 1), Calendar.DATE));
                event.calculateMinutes();

                list.add(event);
            }
        }

        return list;
    }
}
