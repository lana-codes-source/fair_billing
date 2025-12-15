package main.test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.App;
import main.resources.Bill;

public class AppTest {
    @Test
    public void LogTest() {
        String response = App.createBill("src\\main\\test\\resources\\Logs.txt");

        //build correct response
        String correctResponse = new Bill("ALICE99", 4, 240).toString() + "\n" + new Bill("CHARLIE", 3, 37).toString() + "\n";

        assertNotNull(response);
        assertEquals( "response was incorrect" , response, correctResponse);
    }

    @Test
    public void ComplexTest() {
        String response = App.createBill("src\\main\\test\\resources\\ComplexLog.txt");

        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void EmptyPathTest() {

        String response = App.createBill("");
        assertEquals("", response);
        
    }

    @Test
    public void EmptyFileTest() {

        String response = App.createBill("src\\main\\test\\resources\\EmptyLog.txt");
        assertEquals("", response);
        
    }

}
