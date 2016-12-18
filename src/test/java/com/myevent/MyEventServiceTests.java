package com.myevent;

import com.myevent.dao.MyEventMapper;
import com.myevent.domain.MyEvent;
import org.apache.ibatis.annotations.ResultMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyEventServiceTests {
    private Logger logger = LoggerFactory.getLogger(MyEventServiceTests.class);

    @Rule
    public OutputCapture capture = new OutputCapture();

    @MockBean
    private MyEventMapper myEventMapper;

    @Test
    public void findCategory() throws Exception {
        String keyword = "업무회의1";
        logger.info("=== " + keyword);
//        ResultMap map = myEventMapper.findCategory(keyword);
//        assertThat(event, is(nullValue()));

        keyword = "업무회의";
//        map = myEventMapper.findCategory(keyword);
//        logger.info("-- " + event);
//        assertThat(event, not(is(nullValue())));
    }
}
