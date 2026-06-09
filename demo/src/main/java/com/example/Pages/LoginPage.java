package com.example.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.example.Pages.base.BasePage;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//p[contains(text(),'Sign in to your account to continue')]")
    WebElement loginText;

    public boolean isLoginTextDisplayed() {
        return loginText.isDisplayed();
    }

    public void login(String email, String password) throws InterruptedException {
        // Wait for any JS-rendered content to appear
        Thread.sleep(3000);

        // Find email field — try common patterns used by phptravels
        WebElement emailField = findInput(
            "//input[@type='email']",
            "//input[@name='username']",
            "//input[@id='username']",
            "//input[@name='email']",
            "//input[@id='email']",
            "//input[contains(@placeholder,'Email') or contains(@placeholder,'email') or contains(@placeholder,'Username')]"
        );

        WebElement passwordField = findInput(
            "//input[@type='password']",
            "//input[@name='password']",
            "//input[@id='password']"
        );

        WebElement loginButton = findInput(
            "//button[@type='submit']",
            "//input[@type='submit']",
            "//button[contains(text(),'Login') or contains(text(),'Sign in') or contains(text(),'Submit')]"
        );

        emailField.clear();
        emailField.sendKeys(email);
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("Logging in as: " + email);
        loginButton.click();
    }

    // Try each xpath in order, return the first one found
    private WebElement findInput(String... xpaths) {
        for (String xpath : xpaths) {
            try {
                WebElement el = driver.findElement(By.xpath(xpath));
                if (el.isDisplayed()) {
                    System.out.println("Found element with xpath: " + xpath);
                    return el;
                }
            } catch (Exception ignored) {}
        }
        // Last resort: JS to dump all inputs for debugging
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object inputs = js.executeScript(
            "return Array.from(document.querySelectorAll('input')).map(i => i.outerHTML).join('\\n');"
        );
        System.out.println("DEBUG - All inputs on page:\n" + inputs);
        throw new RuntimeException("Could not find element from xpaths: " + String.join(", ", xpaths));
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("Login successful. URL: " + driver.getCurrentUrl());
            return true;
        } catch (Exception e) {
            System.out.println("Login may have failed. URL: " + driver.getCurrentUrl());
            return false;
        }
    }
}
