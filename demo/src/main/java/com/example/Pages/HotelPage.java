package com.example.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.example.Pages.base.BasePage;

public class HotelPage extends BasePage {

    public HotelPage(WebDriver driver) {
        super(driver);
    }

    // --- Home page hotel search form ---
    @FindBy(xpath = "//li[@id='hotels'] | //a[contains(text(),'Hotels')]")
    WebElement hotelsTab;

    @FindBy(xpath = "//input[@id='hotels-destination' or contains(@placeholder,'Destination') or contains(@placeholder,'City')]")
    WebElement destinationInput;

    @FindBy(xpath = "//input[@id='hotels-checkin' or contains(@placeholder,'Check in') or contains(@id,'checkin')]")
    WebElement checkInDate;

    @FindBy(xpath = "//input[@id='hotels-checkout' or contains(@placeholder,'Check out') or contains(@id,'checkout')]")
    WebElement checkOutDate;

    @FindBy(xpath = "//form[contains(@id,'hotels') or contains(@class,'hotel')]//button[contains(@type,'submit') or contains(text(),'Search')]")
    WebElement searchButton;

    // --- Search results ---
    @FindBy(xpath = "(//div[contains(@class,'hotel-item') or contains(@class,'result-item')]//a[contains(@href,'hotel')] | //h3//a[contains(@href,'hotel')])[1]")
    WebElement firstHotelResult;

    // --- Hotel detail / book now ---
    @FindBy(xpath = "//a[contains(text(),'Book Now') or contains(@class,'book-now')] | //button[contains(text(),'Book')]")
    WebElement bookNowButton;

    // --- Booking form fields ---
    @FindBy(xpath = "//input[@name='first_name' or @id='first_name']")
    WebElement bookingFirstName;

    @FindBy(xpath = "//input[@name='last_name' or @id='last_name']")
    WebElement bookingLastName;

    @FindBy(xpath = "//input[@name='email' or @id='email']")
    WebElement bookingEmail;

    @FindBy(xpath = "//input[@name='phone' or @id='phone' or @type='tel']")
    WebElement bookingPhone;

    @FindBy(xpath = "//textarea[@name='address' or @id='address'] | //input[@name='address']")
    WebElement bookingAddress;

    @FindBy(xpath = "//button[@type='submit' and (contains(text(),'Confirm') or contains(text(),'Book') or contains(text(),'Continue'))]")
    WebElement confirmBookingButton;

    public void clickHotelsTab() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(hotelsTab));
            hotelsTab.click();
            System.out.println("Clicked Hotels tab");
        } catch (Exception e) {
            System.out.println("Hotels tab click skipped (may already be active): " + e.getMessage());
        }
    }

    public void enterDestination(String city) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(destinationInput));
        destinationInput.clear();
        destinationInput.sendKeys(city);
        Thread.sleep(1500); // wait for autocomplete dropdown
        // Select first autocomplete suggestion
        try {
            WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class,'autocomplete') or contains(@class,'dropdown')]//li[1] | //div[contains(@class,'suggestion')][1]")
            ));
            suggestion.click();
            System.out.println("Selected city: " + city);
        } catch (Exception e) {
            // No autocomplete shown — press Tab to proceed
            destinationInput.sendKeys("\t");
            System.out.println("No autocomplete for city, continuing: " + city);
        }
    }

    public void setCheckInDate(String date) {
        // Use JS to set flatpickr-controlled inputs
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];", checkInDate, date);
        js.executeScript("arguments[0].dispatchEvent(new Event('change'));", checkInDate);
        System.out.println("Check-in date set: " + date);
    }

    public void setCheckOutDate(String date) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];", checkOutDate, date);
        js.executeScript("arguments[0].dispatchEvent(new Event('change'));", checkOutDate);
        System.out.println("Check-out date set: " + date);
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
        System.out.println("Search clicked");
    }

    public void selectFirstHotel() throws InterruptedException {
        Thread.sleep(3000); // wait for results to load
        wait.until(ExpectedConditions.elementToBeClickable(firstHotelResult));
        System.out.println("Selecting hotel: " + firstHotelResult.getText());
        firstHotelResult.click();
    }

    public void clickBookNow() throws InterruptedException {
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(bookNowButton));
        System.out.println("Clicking Book Now");
        bookNowButton.click();
    }

    public void fillBookingForm(String firstName, String lastName, String email, String phone, String address) {
        wait.until(ExpectedConditions.visibilityOf(bookingFirstName));
        bookingFirstName.clear();
        bookingFirstName.sendKeys(firstName);
        bookingLastName.clear();
        bookingLastName.sendKeys(lastName);
        bookingEmail.clear();
        bookingEmail.sendKeys(email);
        try {
            bookingPhone.clear();
            bookingPhone.sendKeys(phone);
        } catch (Exception e) {
            System.out.println("Phone field not found, skipping");
        }
        try {
            bookingAddress.clear();
            bookingAddress.sendKeys(address);
        } catch (Exception e) {
            System.out.println("Address field not found, skipping");
        }
        System.out.println("Booking form filled for: " + firstName + " " + lastName);
    }

    public void confirmBooking() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmBookingButton));
        System.out.println("Confirming booking...");
        confirmBookingButton.click();
    }
}
