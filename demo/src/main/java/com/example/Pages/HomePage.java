package com.example.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.example.Pages.base.BasePage;

public class HomePage extends BasePage{
    String signinPerson;
    public HomePage(WebDriver driver) {
        super(driver);
    }
    public String typeOfPerson(){
    
       signinPerson="agent";
       //signinPerson="customer";
       return signinPerson;
    }
       
       @FindBy(xpath = "//span[text()='Login']")  
       WebElement loginLink;
       @FindBy(xpath = "//span[text()='Signup']")
       WebElement signUpLink;
       @FindBy(xpath = "//a//span[text()='Customer Signup']")
       WebElement customerSignUpLink;
       @FindBy(xpath="//a//span[text()='Agent Signup']")
       WebElement agentSignUpLink;
       public void hoverOnSignUp()
       {wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
        action.moveToElement(signUpLink).perform();
       }
       public void hoverOnloginLink()
       {wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        action.moveToElement(loginLink).perform();
        //loginLink.click();
       }
       public void clickOncustomerSignUpLink()
       {
       wait.until(ExpectedConditions.elementToBeClickable(customerSignUpLink));
       action.moveToElement(customerSignUpLink).click().perform();
       }
       public void clickOnagentSignUpLink()
       {
       wait.until(ExpectedConditions.elementToBeClickable(agentSignUpLink));
       action.moveToElement(agentSignUpLink).click().perform();
       }
       
}
