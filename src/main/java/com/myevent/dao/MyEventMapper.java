package com.myevent.dao;

import com.myevent.domain.Category;
import com.myevent.domain.GoogleEvent;
import com.myevent.domain.MyEvent;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface MyEventMapper {

    MyEvent findCategory(String keyword);

    List<Category> listCategoryByLevel(int level);

    MyEvent findMyEventById(String id);

    void saveMyEvent(MyEvent event);

    void saveGoogleEvent(GoogleEvent event);

    void removeMyEventById(String id);

    void removeGoogleEventById(String id);

    List<MyEvent> listMyEventWithGoogleEvent(MyEvent event);
}
