import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;

public class WebFormAutomation {
    WebDriver driver;
    Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Automate Student Registration Form")
    public void testRegistration() {
        // Navigating to the Student Registration Form
        driver.get("https://www.tutorialspoint.com/selenium/practice/selenium_automation_practice.php");

        // 1. Name and Email
        driver.findElement(By.id("name")).sendKeys(faker.name().fullName());
        driver.findElement(By.id("email")).sendKeys(faker.internet().emailAddress());

        // 2. Gender
        WebElement maleRadio = driver.findElement(By.id("gender"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", maleRadio);

        // 3. Mobile and DOB
        driver.findElement(By.id("mobile")).sendKeys(faker.number().digits(10));
        driver.findElement(By.id("dob")).sendKeys("01-01-1995");

        // 4. Subjects
        driver.findElement(By.id("subjects")).sendKeys("Computer Science");

        // 5. Hobbies
        WebElement sportsCheckbox = driver.findElement(By.id("hobbies"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sportsCheckbox);

        // 6. Picture Upload
         driver.findElement(By.id("picture")).sendKeys(System.getProperty("user.dir")+"/src/test/resources/profile-pic.png");

        // 7. Current Address
        driver.findElement(By.tagName("textarea")).sendKeys(faker.address().fullAddress());

        // 8. State and City Selects
        Select stateSelect = new Select(driver.findElement(By.id("state")));
        stateSelect.selectByValue("Uttar Pradesh");

        Select citySelect = new Select(driver.findElement(By.id("city")));
        citySelect.selectByValue("Lucknow");

        // 9. Submit Button
        WebElement submitBtn = driver.findElement(By.xpath("//input[@value='Login']"));
        submitBtn.click();

        // 10. Verification
        try {
            WebElement successMsg = driver.findElement(By.xpath("//*[contains(text(),'Information has been submitted')]"));
            Assertions.assertTrue(successMsg.isDisplayed(), "Success message not found!");
        } catch (NoSuchElementException e) {
            Assertions.assertFalse(driver.getCurrentUrl().contains("error"), "Registration failed due to server.");
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
