package main.resources;

import java.time.LocalTime;

public class AccessLog {
    Boolean start, end = false;
    String user;
    LocalTime time;

    public AccessLog() {
    }

    public AccessLog(Boolean s, Boolean e, String u, LocalTime t) {
        start = s;
        end = e;
        user = u;
        time = t;
    }

    public Boolean getStart() {
        return this.start;
    }

    public Boolean getEnd() {
        return this.end;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public String getUser() {
        return this.user;
    }
}
