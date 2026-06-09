package com.example.testcases;

import com.example.Pages.HotelPage;
import com.example.Pages.LoginPage;
import com.example.Pages.SignUpPage;
import com.example.testBase.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingTest extends BaseTest {

    // Demo credentials for phptravels.net
    private static final String USER_EMAIL    = "user@phptravels.net";
    private static final String USER_PASSWORD = "demouser";

    @Test(priority = 1)
    public void loginTest() throws InterruptedException {
        driver.get("https://phptravels.com/demo");
        System.out.println("Navigated to login page");

        LoginPage lp = new LoginPage(driver);
        lp.login(USER_EMAIL, USER_PASSWORD);

        boolean success = lp.isLoginSuccessful();
        Assert.assertTrue(success, "Login should succeed with demo credentials");
    }

    @Test(priority = 2, dependsOnMethods = "loginTest")
    public void searchAndBookHotel() throws InterruptedException {
        driver.get("https://phptravels.net/");
        System.out.println("Navigated to home page for hotel search");

        HotelPage hp = new HotelPage(driver);

        // Click Hotels tab if needed
        hp.clickHotelsTab();
        Thread.sleep(500);

        // Enter destination
        hp.enterDestination("Dubai");

        // Set dates (format: MM/DD/YYYY as used by phptravels)
        hp.setCheckInDate("07/01/2026");
        hp.setCheckOutDate("07/05/2026");

        // Search
        hp.clickSearch();
        System.out.println("Waiting for hotel search results...");

        // Select first result
        hp.selectFirstHotel();
        System.out.println("Opened hotel detail page");

        // Book
        hp.clickBookNow();
        System.out.println("On booking form");

        // Fill booking details
        String firstName = SignUpPage.getRandomName().split(" ")[0];
        String lastName  = SignUpPage.getRandomName().split(" ")[1];
        hp.fillBookingForm(
            firstName,
            lastName,
            SignUpPage.getRandomNameWithNumber() + "@gmail.com",
            "9876543210",
            "123 Test Street, Demo City"
        );

        Thread.sleep(1000);

        // NOTE: Uncomment the line below to actually submit the booking.
        // Kept commented to avoid placing real/demo bookings unintentionally.
        // hp.confirmBooking();

        System.out.println("Booking form filled successfully for: " + firstName + " " + lastName);
        System.out.println("Current URL: " + driver.getCurrentUrl());
    }
}
