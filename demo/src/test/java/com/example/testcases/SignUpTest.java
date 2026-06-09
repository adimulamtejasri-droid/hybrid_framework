package com.example.testcases;

import com.example.Pages.SignUpPage;
import com.example.testBase.BaseTest;
import org.testng.annotations.Test;

public class SignUpTest extends BaseTest {

    @Test
    public void verifySignUpPage() throws InterruptedException {
        driver.get("https://phptravels.com/demo");
        System.out.println("Navigated to phptravels demo request form");
        Thread.sleep(3000); // wait for JS to render

        SignUpPage sp = new SignUpPage(driver);

        String[] nameParts = SignUpPage.getRandomName().split(" ");
        sp.first_name(nameParts[0]);
        sp.last_name(nameParts[1]);
        sp.company_name("TestCo" + SignUpPage.getRandomNumber(10, 99));
        sp.whatsapp_number("9" + SignUpPage.getRandomNumber(100000000, 999999999));
        sp.emailID(SignUpPage.getRandomNameWithNumber() + "@gmail.com");

        Thread.sleep(1000); // let captcha render
        sp.solveCaptchaAndEnter();

        Thread.sleep(1000);
        System.out.println("Form filled. Current URL: " + driver.getCurrentUrl());

        // NOTE: Uncomment to actually submit the demo request form
         sp.submit();
    }
}
