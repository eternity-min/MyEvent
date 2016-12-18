package com.myevent.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.myevent.dao.MyEventMapper;
import com.myevent.domain.Category;
import com.myevent.domain.MyEvent;
import com.myevent.util.GoogleCalendarUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class MyEventService {

    @Autowired
    private MyEventMapper myEventMapper;

    public List<MyEvent> listEventsFromGoogleCalendar(Date start, Date end) throws IOException {
        List<MyEvent> list = new ArrayList<MyEvent>();
        List<Event> items = GoogleCalendarUtils.listEvent(start, end);

        for (Event item : items) {
            if (item.getId().equals("0s5ukr3f7g0qev1s1rnnt63n1o")) {
                continue;
            }

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
}
