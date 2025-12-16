package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.App;
import main.resources.Bill;

public class AppTest {
    @Test
    public void LogTest() {
        String response = App.createBill("src\\test\\resources\\Logs.txt");

        //build correct response
        String correctResponse = new Bill("ALICE99", 4, 240).toString() + System.lineSeparator() + new Bill("CHARLIE", 3, 37).toString();

        assertNotNull(response);
        assertEquals( "response was incorrect" , response, correctResponse);
    }

    @Test
    public void ComplexTest() {
        String response = App.createBill("src\\test\\resources\\ComplexLog.txt");

        assertNotNull(response);
    }

    @Test
    public void EmptyPathTest() {

        String response = App.createBill("");
        assertEquals("error occured while reading file. The error was: java.io.FileNotFoundException: ", response);
        
    }

    @Test
    public void EmptyFileTest() {

        String response = App.createBill("src\\test\\resources\\EmptyLog.txt");
        assertEquals("No complete log lines found.", response);
        
    }

}
