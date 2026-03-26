import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class TableScraper {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Scrape DSE Market Table Data")
    public void scrapeDseData() throws IOException {
        driver.get("https://dsebd.org/latest_share_price_scroll_by_value.php");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(d -> d.findElement(By.className("table-responsive")).isDisplayed());

        // Locate the table rows
        List<WebElement> rows = driver.findElements(By.xpath("//table[contains(@class, 'table')]//tr"));

        try (FileWriter writer = new FileWriter("./src/test/resources/DSE_Market_Data.txt")) {
            // Write the Header
            String header = "# | TRADING CODE | LTP* | HIGH | LOW | CLOSEP* | YCP* | CHANGE | TRADE | VALUE (mn) | VOLUME\n";
            System.out.println(header);
            writer.write(header);

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                // DSE data rows typically have 11 columns
                if (cells.size() >= 11) {
                    StringBuilder rowData = new StringBuilder();

                    for (int i = 0; i < cells.size(); i++) {
                        String text = cells.get(i).getText().trim().replace(",", "");
                        rowData.append(text);

                        // Add the separator unless it's the last element
                        if (i < cells.size() - 1) {
                            rowData.append(" | ");
                        }
                    }

                    String finalLine = rowData.toString()+ "\n";;

                    // Print to console and write to file
                    System.out.println(finalLine);
                    writer.write(finalLine);
                }
            }
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}