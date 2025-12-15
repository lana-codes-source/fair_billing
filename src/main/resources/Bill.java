package main.resources;

public class Bill {
    String user;
    int sessionCount;
    int minimumDuration;

    public Bill(String u, int dur) {
        user = u;
        sessionCount = 1;
        minimumDuration = dur;
    }

    public Bill(String u, int c, int dur) {
        user = u;
        sessionCount = c;
        minimumDuration = dur;
    }

    public Bill Add(int dur) {
        this.minimumDuration += dur;
        this.sessionCount ++;
        return this;
    }

    @Override
    public String toString() {
        return (this.user + " " + this.sessionCount + " " + this.minimumDuration);
    }
}
