A program which takes text based log files and returns the duration and amount of sessions that a user had during that time.

log files are processed per line, it is assumed that the username will always be the first text not including the timestamp.
Log lines should be in the format as follows

```
14:02:03 ALICE99 Start
14:02:05 CHARLIE End
```

and lines without a valid timestamp, username and either "Start" or "End" will be ignored.
The relative path must be entered once prompted, and entering "stop" will end the program.