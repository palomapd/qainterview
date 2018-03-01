package com.seleniumtest;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class InterviewTest {

    private static final String HOME_URL = "http://computer-database.herokuapp.com/computers";
    private static final String ADD_URL = "http://computer-database.herokuapp.com/computers/new";

    private WebDriver driver;

    public InterviewTest() {
        System.setProperty("webdriver.gecko.driver", "/usr/bin/chromedriver");
    }

    @Before
    public void setUp() {
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void testRedirectHome() {
        // GIVEN
        driver.get(ADD_URL);

        // WHEN
        driver.findElement(By.linkText("Play sample application — Computer database")).click();

        // THEN
        assertEquals(HOME_URL, driver.getCurrentUrl());
    }

    @Test
    public void testCrud() {
        String computerName = "Computer-" + new Random().nextInt();
        String computerName2 = "Computer-" + new Random().nextInt();


        // Go to add screen

        // GIVEN
        driver.get(HOME_URL);
        assertEquals("Computers database", driver.getTitle());
        WebElement addElement = driver.findElement(By.id("add"));
        assertEquals("Add a new computer", addElement.getText());

        // WHEN
        addElement.click();

        // THEN
        assertEquals(ADD_URL, driver.getCurrentUrl());



        // Create computer

        // WHEN
        WebElement computerNameInput = driver.findElement(By.id("name"));
        computerNameInput.sendKeys(computerName);
        WebElement createButton = driver.findElement(By.className("primary"));
        createButton.click();

        // THEN
        assertEquals(HOME_URL, driver.getCurrentUrl());
        WebElement alertMessage = driver.findElement(By.className("alert-message"));
        assertEquals("Done! Computer " + computerName + " has been created", alertMessage.getText());



        // Filter on computer name

        // WHEN
        WebElement searchBox = driver.findElement(By.id("searchbox"));
        searchBox.sendKeys(computerName);
        WebElement searchButton = driver.findElement(By.id("searchsubmit"));
        searchButton.click();

        // THEN
        assertEquals(HOME_URL + "?f=" + computerName, driver.getCurrentUrl());
        driver.findElement(By.xpath("//h1[text()='One computer found']"));
        driver.findElement(By.xpath("//a[text()='" + computerName + "']"));



        // Update computer name

        // GIVEN
        WebElement computerLink = driver.findElement(By.xpath("//a[text()='" + computerName + "']"));

        // WHEN
        computerLink.click();

        // THEN
        driver.findElement(By.xpath("//h1[text()='Edit computer']"));
        computerNameInput = driver.findElement(By.id("name"));
        assertEquals(computerName, computerNameInput.getAttribute("value"));

        // WHEN
        computerNameInput.clear();
        computerNameInput.sendKeys(computerName2);
        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Save this computer']"));
        submitButton.click();

        // THEN
        assertEquals(HOME_URL, driver.getCurrentUrl());
        alertMessage = driver.findElement(By.className("alert-message"));
        assertEquals("Done! Computer " + computerName2 + " has been updated", alertMessage.getText());



        // Filter on computer name

        // WHEN
        searchBox = driver.findElement(By.id("searchbox"));
        searchBox.sendKeys(computerName2);
        searchButton = driver.findElement(By.id("searchsubmit"));
        searchButton.click();

        // THEN
        assertEquals(HOME_URL + "?f=" + computerName2, driver.getCurrentUrl());
        driver.findElement(By.xpath("//h1[text()='One computer found']"));
        driver.findElement(By.xpath("//a[text()='" + computerName2 + "']"));



        // Delete computer

        // GIVEN
        computerLink = driver.findElement(By.xpath("//a[text()='" + computerName2 + "']"));

        // WHEN
        computerLink.click();

        // THEN
        driver.findElement(By.xpath("//h1[text()='Edit computer']"));
        WebElement deleteButton = driver.findElement(By.xpath("//input[@value='Delete this computer']"));

        // WHEN
        deleteButton.click();

        // THEN
        assertEquals(HOME_URL, driver.getCurrentUrl());
        alertMessage = driver.findElement(By.className("alert-message"));
        assertEquals("Done! Computer has been deleted", alertMessage.getText());
    }
}
