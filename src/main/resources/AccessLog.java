package main.resources;

import java.time.LocalTime;

public class AccessLog {
    public Boolean start, end = false;
    public String user;
    public LocalTime time;

    public AccessLog() {
    }

    public AccessLog(Boolean s, Boolean e, String u, LocalTime t) {
        start = s;
        end = e;
        user = u;
        time = t;
    }
}
