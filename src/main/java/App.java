package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.StringJoiner;

import main.resources.AccessLog;
import main.resources.Bill;

public class App {

    private static final String timestamp = "[0-9]{2}:[0-9]{2}:[0-9]{2}";
    private static final String username = "^[a-zA-Z0-9]+?$";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to log file, enter stop when finished:");

        while (scanner.hasNext()) {
            String input = scanner.next();
            if (input.equalsIgnoreCase("stop")) {
                break;
            } else {
                System.out.println(createBill(input));
            }
        }
        scanner.close();

    }

    /*
    Creates a Bill based on the log file input
    which contains the username, a count of sessions
    and the total duration of the sessions.
    Returns an empty string for invalid unputs.
    */
    public static String createBill(String path) {
        
        StringJoiner bill = new StringJoiner(System.lineSeparator(), "", "");
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
            Boolean containsStart = false, containsEnd = false, containsTime = false;
            String userName = "";
            LocalTime time = LocalTime.MAX;

            if (currentLine.contains("Start") || currentLine.contains("End")) {
                var strings = currentLine.split(" ");
                for (String s : strings ) {
                if (s.matches(timestamp)) {
                    time = LocalTime.parse(s);
                    containsTime = true;
                } else if (s.matches(username) && userName.isBlank()) {
                    userName = s;
                } else if (s.equals("Start")) {
                    containsStart = true;
                } else if (s.equals("End")) {
                    containsEnd = true;
                } 
            }
            }
            if (!userName.isEmpty() && containsTime && (containsStart || containsEnd)) {
                logList.add(new AccessLog(containsStart, containsEnd, userName, time));
            }
        }
        } catch (Exception e) {
            System.out.println("An error occured while processing the file. The error was: " + e);
            return "";
        }

        if (logList.isEmpty()) {
            System.out.println("No complete log lines found.");
            return "";
            return "";
        }

        if (logList.isEmpty()) {
            System.out.println("No complete log lines found.");
            return "";
        }

        LocalTime first = logList.getFirst().getTime();
        LocalTime last = logList.getLast().getTime();
        ListIterator<AccessLog> logIterator = logList.listIterator();
        LinkedList<AccessLog> logCopy = new LinkedList<>(logList);

        
        //Iterates through start logs only and cleans up any leftover ends afterwards
        while (logIterator.hasNext()) {
            AccessLog log = logIterator.next();
            if (!!log.getStart()) {
                int duration = -1;
                AccessLog endLog = new AccessLog();
                logCopy.remove(log);

                for ( AccessLog l : logCopy ) {
                    if ( l.getEnd() && l.getUser().equals(log.getUser()) && l.getTime().isAfter(log.getTime()) ) {
                        duration = Math.toIntExact(SECONDS.between(log.getTime(), l.getTime()));
                        endLog = l;
                        break;
                    }
                }
                logCopy.remove(endLog);   
                //if no matching end time is found use the last time
                if (duration == -1) {
                    duration = Math.toIntExact(SECONDS.between(log.getTime(), last));
                }

            if (usersAndBill.get(log.getUser()) == null ) {
                usersAndBill.put(log.getUser(), new Bill(log.getUser(), duration));
            } else {
                usersAndBill.put(log.getUser(), usersAndBill.get(log.getUser()).Add(duration));
            }
            }
        }

        //end time log, only happens if start time was in previous log file.
        for ( AccessLog l : logCopy ) {
            if (!!l.getEnd()) {
                int duration = Math.toIntExact(SECONDS.between(first, l.getTime()));
                if (usersAndBill.get(l.getUser()) == null ) {
                    usersAndBill.put(l.getUser(), new Bill(l.getUser(), duration));
                } else {
                    usersAndBill.put(l.getUser(), usersAndBill.get(l.getUser()).Add(duration));
            }
            }
        }

        //put together complete bill
        for ( Bill values : usersAndBill.values()) {
            bill.add(values.toString());
        }

        return bill.toString();

    }
}

