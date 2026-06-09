package com.example.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.example.Pages.base.BasePage;
import java.util.Locale;
import java.util.Random;

public class SignUpPage extends BasePage {

    static Random random = new Random();

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    // phptravels.com/demo form — fields identified by CSS class
    @FindBy(xpath = "//input[contains(@class,'first_name')]")
    WebElement firstName;

    @FindBy(xpath = "//input[contains(@class,'last_name')]")
    WebElement lastName;

    @FindBy(xpath = "//input[contains(@class,'company_name')]")
    WebElement companyName;

    @FindBy(xpath = "//input[contains(@class,'whatsapp_number')]")
    WebElement whatsappNumber;

    @FindBy(xpath = "//input[contains(@class,'email') and not(contains(@class,'hidden'))]")
    WebElement emailField;

    // Captcha: label holds the math question, input[@id='number'] holds the answer
    @FindBy(xpath = "//label[contains(@for,'number')] | //p[contains(text(),'What is')] | //*[contains(@class,'math-question') or contains(@class,'captcha')]")
    WebElement captchaLabel;

    @FindBy(xpath = "//input[@id='number' or contains(@class,'math')]")
    WebElement captchaAnswer;

    @FindBy(xpath = "//button[@id='demo' or contains(@class,'btn_submit')]")
    WebElement submitButton;

    // --- Agent signup on phptravels.net (kept for when the site is back) ---
    @FindBy(xpath = "//a//span[text()='person_add']")
    WebElement getStarted;

    public void clickOnGetStarted() {
        wait.until(ExpectedConditions.elementToBeClickable(getStarted));
        String originalUrl = driver.getCurrentUrl();
        getStarted.click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(originalUrl)));
        String newUrl = driver.getCurrentUrl();
        if (newUrl.contains("agent-signup")) {
            System.out.println("Navigated to: " + newUrl);
        } else {
            throw new AssertionError("Expected agent-signup URL but got: " + newUrl);
        }
    }

    // --- Random test data helpers ---
    public static int getRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static String getRandomName() {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona", "George", "Hannah"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};
        return firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
    }

    public static String getRandomNameWithNumber() {
        return getRandomName().replace(" ", "") + getRandomNumber(100, 999);
    }

    // --- Form fill methods ---
    public void first_name(String firstname) {
        wait.until(ExpectedConditions.visibilityOf(firstName));
        firstName.clear();
        firstName.sendKeys(firstname);
        System.out.println("First name entered: " + firstname);
    }

    public void last_name(String lastname) {
        lastName.clear();
        lastName.sendKeys(lastname);
        System.out.println("Last name entered: " + lastname);
    }

    public void company_name(String company) {
        try {
            companyName.clear();
            companyName.sendKeys(company);
            System.out.println("Company name entered: " + company);
        } catch (Exception e) {
            System.out.println("Company name field not present, skipping");
        }
    }

    public void whatsapp_number(String phone) {
        try {
            whatsappNumber.clear();
            whatsappNumber.sendKeys(phone);
            System.out.println("WhatsApp number entered: " + phone);
        } catch (Exception e) {
            System.out.println("WhatsApp field not present, skipping");
        }
    }

    public void emailID(String email) {
        emailField.clear();
        emailField.sendKeys(email);
        System.out.println("Email entered: " + email);
    }

    public void solveCaptchaAndEnter() {
        try {
            // Dump the label nearest to the captcha input for debugging
            WebElement captchaInput = driver.findElement(By.xpath("//input[@id='number' or contains(@class,'math')]"));
            // Try sibling/parent label or any nearby text node containing a number pattern
            String questionText = null;

            // Strategy 1: label[@for='number']
            try {
                questionText = driver.findElement(By.xpath("//label[@for='number']")).getText();
            } catch (Exception ignored) {}

            // Strategy 2: any element near the input containing digits and operator words
            if (questionText == null || questionText.isBlank()) {
                try {
                    questionText = driver.findElement(By.xpath(
                        "//*[contains(@class,'math') or contains(@class,'captcha') or contains(@class,'question')]"
                    )).getText();
                } catch (Exception ignored) {}
            }

            // Strategy 3: JS — get text of nearest container to the captcha input
            if (questionText == null || questionText.isBlank()) {
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                questionText = (String) js.executeScript(
                    "var el = document.querySelector('input#number, input.math');" +
                    "if (!el) return '';" +
                    "var label = el.closest('div,section,form');" +
                    "return label ? label.innerText : '';"
                );
            }

            if (questionText != null && !questionText.isBlank()) {
                System.out.println("Captcha question text found: " + questionText);
                int answer = captcha_solving(questionText.trim());
                System.out.println("Captcha answer: " + answer);
                captchaInput.clear();
                captchaInput.sendKeys(String.valueOf(answer));
            } else {
                System.out.println("Captcha question text could not be found");
            }
        } catch (Exception e) {
            System.out.println("Could not solve captcha: " + e.getMessage());
        }
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        System.out.println("Form submitted");
    }

    public static int captcha_solving(String captcha_question) throws Exception {
        String[] words = captcha_question.toLowerCase().replace("?", "").trim().split("\\s+");

        // Supports two formats:
        //   "N op N = ?"      (phptravels.com/demo)  → indices 0, 1, 2
        //   "What is N op N?" (phptravels.net)        → indices 2, 3, 4
        boolean isShortFormat = words.length <= 4 || !words[0].equalsIgnoreCase("what");
        int n1idx = isShortFormat ? 0 : 2;
        int opIdx = isShortFormat ? 1 : 3;
        int n2idx = isShortFormat ? 2 : 4;

        int num1;
        try {
            num1 = Integer.parseInt(words[n1idx]);
        } catch (NumberFormatException e) {
            RuleBasedNumberFormat fmt = new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.SPELLOUT);
            num1 = fmt.parse(words[n1idx]).intValue();
        }

        String op = words[opIdx];

        int num2;
        try {
            num2 = Integer.parseInt(words[n2idx]);
        } catch (NumberFormatException e) {
            RuleBasedNumberFormat fmt = new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.SPELLOUT);
            num2 = fmt.parse(words[n2idx]).intValue();
        }

        switch (op) {
            case "plus":       case "+": return num1 + num2;
            case "minus":      case "-": return num1 - num2;
            case "times":      case "multiplied": case "*": return num1 * num2;
            case "divided":    case "/": return num1 / num2;
            default: throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }
}
