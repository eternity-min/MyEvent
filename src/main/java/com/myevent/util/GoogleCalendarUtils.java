package com.myevent.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class GoogleCalendarUtils {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    //private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".credentials/calendar-java-quickstart");
    private static File DATA_STORE_DIR;

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    //private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);

    static {
//        System.setProperty("http.proxyHost", "72.50.150.6");
//        System.setProperty("http.proxyPort", "8080");
//        System.setProperty("https.proxyHost", "72.50.150.6");
//        System.setProperty("https.proxyPort", "8080");
//
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }
                }
        };
//
//        // Install the all-trusting trust manager
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS"); // SSL or TSL
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        try {
            //HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            SSLContext sc = SSLContext.getInstance("SSL"); // SSL or TSL
            sc.init(null, trustAllCerts, null);

            HTTP_TRANSPORT = new NetHttpTransport.Builder()
                    .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("72.50.150.6", 8080)))
                    .trustCertificates(GoogleUtils.getCertificateTrustStore())
                    .setSslSocketFactory(sc.getSocketFactory())
                    .build();

            DATA_STORE_DIR = new File(GoogleCalendarUtils.class.getResource("/google").toURI());
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarUtils.class.getResourceAsStream("/google/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static Calendar getCalendarService() throws IOException {
        Credential credential = authorize();
        return new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<Event> listEvent(Date start, Date end) throws IOException {
        Calendar service = getCalendarService();
        Events events = service.events().list("primary")
                //.setMaxResults(10)
                .setTimeMin(new DateTime(start))
                .setTimeMax(new DateTime(end))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setCalendarId("eternity6170@gmail.com")
                .execute();
        return events.getItems();
    }

    public static Event updateSummary(String id, String summary) throws IOException {
        Calendar service = getCalendarService();
        Event event = service.events().get("primary", id).execute();
        event.setSummary(summary);
        Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

//        System.out.println(summary);
//        System.out.println(event);
//        System.out.println(updatedEvent);

        return updatedEvent;
    }
}