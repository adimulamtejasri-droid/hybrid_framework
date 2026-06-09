package com.example.Pages.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
     public WebDriver driver;
     public Actions action;
     public WebDriverWait wait;
      public BasePage(WebDriver driver)
      {
        this.driver=driver;
        this.action=new Actions(driver);
        this.wait= new WebDriverWait(driver,Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);

      }       
}
