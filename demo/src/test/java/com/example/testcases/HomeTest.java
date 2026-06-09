package com.example.testcases;
//import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.example.Pages.HomePage;
import com.example.Pages.LoginPage;
import com.example.Pages.SignUpPage;
import com.example.testBase.BaseTest;
/*import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;*/

public class HomeTest extends BaseTest{
    HomePage hm;
    LoginPage lp;
    SignUpPage sp;
    @BeforeMethod
    public void homepage()
    {      
           hm=new HomePage(driver);
           lp=new LoginPage(driver);
           sp=new SignUpPage(driver);

    }
         
   
    @Test(priority=1)
    public void verifyTitle() {
        driver.get("https://phptravels.net/");
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        
    }
    @Test(priority=2)
    public void verifySignUp() throws InterruptedException
    {   
        hm.hoverOnSignUp();
        System.out.println("clicked signup successfully");
        Thread.sleep(500); // Wait for submenu to appear
        if(hm.typeOfPerson().contains("agent")){
        hm.clickOnagentSignUpLink();
        System.out.println("clicked agent signup successfully");
        }
        else{
        hm.clickOncustomerSignUpLink();
        System.out.println("clicked signup successfully");
        }
       Thread.sleep(2000); // Wait for page to load
       String currentUrl = driver.getCurrentUrl();
       System.out.println("Current URL: " + currentUrl);
        sp.clickOnGetStarted();
        System.out.println("clicked get started successfully");

    }
    @Test(dependsOnMethods = "verifySignUp" , priority=1)
    public void verifyLogin()
    {   driver.get("https://phptravels.net/");
        hm.hoverOnloginLink();
        System.out.println("Logged in clicked successfully");
        //Assert.assertFalse(lp.isLoginTextDisplayed(),"Login text is not displayed");
        
    }
}
