package com.myevent;

import com.myevent.domain.MyEvent;
import com.myevent.service.MyEventService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterMHTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MyEventService myEventService;

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private static JavascriptExecutor js;  // JavascriptExecutor 클래스 선언
    private static Date startDate;
    private static Date endDate;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.ie.driver", "D:\\Work\\intelliJIDEA\\GoogleCalendar\\IEDriverServer.exe");

//        driver = new FirefoxDriver();
        driver = new InternetExplorerDriver();
        js = (JavascriptExecutor) driver;

        baseUrl = "http://72.2.180.132:18100";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseUrl + "/everSys.jsp");
        Alert alert = driver.switchTo().alert();
        alert.accept();


        String searchStart = "2017-03-02";
        String searchEnd = "2017-03-03";

        String[] datePattern = new String[]{"yyyyMMddhhmmss", "yyyy-MM-dd hh:mm:ss", "yyyyMMdd", "yyyy-MM-dd"};
        startDate = DateUtils.parseDate(searchStart, datePattern);
        endDate = DateUtils.parseDate(searchEnd, datePattern);

        // google event save to local
        List<MyEvent> myEvents = myEventService.listEventsFromGoogleCalendar(startDate, endDate);
        for (MyEvent item : myEvents) {
            if (StringUtils.isBlank(item.getCategory1())) {
                throw new RuntimeException("Data is not validate - " + ToStringBuilder.reflectionToString(item));
            }

            myEventService.saveEvent(item);
        }
    }

    @Test
    public void registerList() throws Exception {
        List<MyEvent> myEvents = myEventService.listMyEventWithMinutes(startDate, endDate);

        Date starttime = null, endtime = null;
        for (MyEvent item : myEvents) {
            starttime = new Date();
            logger.info("Start - " + ToStringBuilder.reflectionToString(item));
            driver.get(baseUrl + "/eversys.do?method=checkSys1&jobDate=" + DateFormatUtils.format(item.getStart(), "yyyyMMdd"));

            //clear();
            register(item);

            try {
                Alert alert = driver.switchTo().alert();
                logger.info(alert.getText());
                alert.accept();

                if (!"저장이 완료되었습니다.".equals(alert.getText())) {
                    throw new RuntimeException("등록 에러 발생");
                }
            } catch (NoAlertPresentException e) {
                logger.info("alert is not present");
            }

            endtime = new Date();
            logger.info("End - " + ((endtime.getTime() - starttime.getTime()) / 1000));
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private void clear() throws Exception {
        String script = "$('img[src=\"http://72.2.180.132:18100/images/dc_sj.gif\"]')";
        Object result = js.executeScript("return " + script + ".length");
        if (result == null) {
            return;
        }

        long length = ((Long) result).longValue();
        if (length < 1) {
            return;
        }

        js.executeScript(script + "[" + (length-1) + "].click();");
        closeAlertAndGetItsText();
    }

    private void register(MyEvent item) throws Exception {
        // 카테고리 선택
        String mhlv1Script = "var radiomhLv1 = $('#' + $('label:contains(\"" + item.getCategory1() + "\")').prop('for'));"
                + "radiomhLv1.click(); "
                + "check_select(radiomhLv1.val(), 'U');";
        String mhlv2Script = "var selmhLv2 = $('select[name=mhLv2] > option[text=\"" + item.getCategory2() + "\"]');"
                + "selmhLv2.prop('selected', true);"
                + "check_select2(selmhLv2.val(), 'U');";
        String mhlv3Script = "var selmhLv3 = $('select[name=mhLv3] > option[text=\"" + item.getCategory3() + "\"]');"
                + "selmhLv3.prop('selected', true);"
                + "check_select3(selmhLv3.val());";

//        if ("근태관리".equals(item.getCategory1())) {
//            mhlv1Script = "var radiomhLv1 = $('input:radio[value=mhLv640]');"
//                    + "radiomhLv1.click(); "
//                    + "check_select(radiomhLv1.val(), 'U');";
//        }

        executeScriptAndWait(mhlv1Script, "mhLv2", 1);
        executeScriptAndWait(mhlv2Script, "mhLv3", 1);
        js.executeScript(mhlv3Script);

        // 업무시간
        String timeType = "업무시간내";
        int startHour = Integer.valueOf(DateFormatUtils.format(item.getStart(), "HH"));
        if (startHour > 18) {
            timeType = "업무시간외(심야)";
        } else if (startHour < 9) {
            timeType = "업무시간외";
        }
        String mhTy9ChkScript = "var mhTy9Chk = $('#' + $('label:contains(\"" + timeType + "\")').prop('for'));mhTy9Chk.click();";
        js.executeScript(mhTy9ChkScript);

        // 그외 값 입력
        if (StringUtils.isNotBlank(item.getSubsystem())) {
            js.executeScript("$('select[name=systemCd] > option:contains(\"인터넷면세점\\(e-Commerce\\)\")').prop('selected', true)");
        }
        if (StringUtils.isNotBlank(item.getSrId())) {
            driver.findElement(By.id("srNo")).sendKeys(item.getSrId());
        }
        driver.findElement(By.id("comments")).sendKeys(StringUtils.substringBefore(item.getContent(), ";"));
        driver.findElement(By.id("srClient")).sendKeys(item.getRequester());
        driver.findElement(By.id("mh")).sendKeys(String.valueOf(item.getMinutes()));

        js.executeScript("fncUpdateJob();");
    }

    private boolean executeScriptAndWait(String script, String waitMhlvName, int count) throws Exception {
        if (count == 1) {
            logger.info("re-run : " + script);
            js.executeScript(script);
        } else if (count % 10 == 0) {
            logger.info("re-run : " + script);
            js.executeScript(script);
            Thread.sleep(1000);
        } else if (count > 50) {
            throw new RuntimeException("하위항목 미노출");
        }

        String checkscript = "return $('select[name=" + waitMhlvName + "] > option').length";
        logger.info(count + " : " + checkscript);

        Object result = js.executeScript(checkscript);
        logger.info(ToStringBuilder.reflectionToString(result));

        if (result instanceof Long) {
            if (((Long) result).longValue() > 1) {
                return true;
            }
        }

        return executeScriptAndWait(script, waitMhlvName, ++count);
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}