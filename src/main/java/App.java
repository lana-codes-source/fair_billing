package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import main.resources.AccessLog;
import main.resources.Bill;

public class App {

    private static final String timestamp = "[0-9]{2}:[0-9]{2}:[0-9]{2}";
    private static final String username = "^[a-zA-Z0-9]+?$";

    public static void main(String[] args) throws Exception {
        //add a scanner to capture inputs until "stop" is input
        System.out.println(createBill("src\\main\\resources\\Logs.txt"));
    }

    //take the path to the log file
    //ues command line to grab said path
    //iterate over each line
    //split the line of text per space
    //check if string map contains "start" or "end"
    //ignore if timestamp and user is not there (ignore if no time, user or start/end)
    //create bill for each user
    //if user exists update counts
    public static String createBill(String path) {
        
        StringBuilder bill = new StringBuilder();
        HashMap<String, Bill> usersAndBill = new HashMap<>();
        LinkedList<AccessLog> logList = new LinkedList<>();
        FileReader file;

        try {
        file = new FileReader(path);
        } catch (Exception e) {
            System.out.println("error occured while reading file. The error was: " + e);
            return "";
        }

        try {
            BufferedReader br = new BufferedReader(file);
            String currentLine;

            //process all lines of txt into a linked map of records
            while ((currentLine = br.readLine()) != null) {
            Boolean containsStart = false, containsEnd = false;
            String userName = "";
            LocalTime time = LocalTime.MIN;

            if (currentLine.contains("Start") || currentLine.contains("End")) {
                var strings = currentLine.split(" ");
                for (String s : strings ) {
                if (s.matches(timestamp)) {
                    time = LocalTime.parse(s);
                } else if (s.matches(username) && userName.isBlank()) {
                    userName = s;
                } else if (s.equals("Start")) {
                    containsStart = true;
                } else if (s.equals("End")) {
                    containsEnd = true;
                } 
            }
            }
            if (!userName.isEmpty() && time != null && (containsStart || containsEnd)) {
                logList.add(new AccessLog(containsStart, containsEnd, userName, time));
            }
        }
        } catch (Exception e) {
            System.out.println("An error occured while processing the file. The error was: " + e);
        }

        LocalTime first = logList.getFirst().time;
        LocalTime last = logList.getLast().time;
        ListIterator<AccessLog> logIterator = logList.listIterator();
        LinkedList<AccessLog> logCopy = new LinkedList<AccessLog>(logList);

        
        //use logList to create Bill
        //Iterates through starts only and cleans up any leftover ends afterwards
        while (logIterator.hasNext()) {
            //duration is defaulted to -1 as it could never be set to that later in the code.
            AccessLog log = logIterator.next();
            if (!!log.start) {
                int duration = -1;
                AccessLog endLog = new AccessLog();
                logCopy.remove(log);

                for ( AccessLog l : logCopy ) {
                    if ( l.end && l.user.equals(log.user) && l.time.isAfter(log.time) ) {
                        duration = Math.toIntExact(SECONDS.between(log.time, l.time));
                        endLog = l;
                        break;
                    }
                }
                logCopy.remove(endLog);   
                //if no matching end time is found use the last time
                if (duration == -1) {
                    duration = Math.toIntExact(SECONDS.between(log.time, last));
                }

                //find and set first record
            if (usersAndBill.get(log.user) == null ) {
                usersAndBill.put(log.user, new Bill(log.user, duration));
            } else {
                usersAndBill.put(log.user, usersAndBill.get(log.user).Add(duration));
            }
            }
        }

        //end time log, only happens if start time was in previous log file.
        for ( AccessLog l : logCopy ) {
            if (!!l.end) {
                int duration = Math.toIntExact(SECONDS.between(first, l.time));
                if (usersAndBill.get(l.user) == null ) {
                    usersAndBill.put(l.user, new Bill(l.user, duration));
                } else {
                    usersAndBill.put(l.user, usersAndBill.get(l.user).Add(duration));
            }
            }
        }

        //put together complete bill
        for ( Bill values : usersAndBill.values()) {
            bill.append(values).append("\n");

        }

        return bill.toString();

    }
}

