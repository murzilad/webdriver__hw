package search;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckFirstSearchResult_Test {

    private final static Logger logger = LogManager.getLogger(CheckFirstSearchResult_Test.class);
    private WebDriver driver;


    //перед каждым тестом запускается установка webdriver
    @BeforeEach
    public void driverSetup() {
        logger.trace("Скачивание ВебДрайвера начато");
        WebDriverManager.chromedriver().setup();
        logger.trace("Скачивание ВебДрайвера завершено");
    }

    //после каждого теста закрывается окно браузера, если не закрыто
    @AfterEach
    public void tearDown() {
        logger.trace("Закрытие браузера начато");
        if (driver != null)
            driver.quit();
        logger.trace("Закрытие браузера завершено");
    }

    @Test
    public void checkFirstSearchResult() {

        String expectedText = "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...";
        String searchBoxSelector = "searchbox_input";
        String searchButtonSelector = "button[type = 'submit']";
        String searchFirstResultSelector = "ol li:first-child div h2 span";

        logger.trace("Открытие браузера начато");
        ChromeOptions options = new ChromeOptions();

        //открыть Chrome в headless режиме
        options.addArguments("headless");

        driver = new ChromeDriver(options);
        logger.trace("Открытие браузера завершено");
        logger.trace("Открытие сайта начато");
        driver.get("https://duckduckgo.com/");
        logger.trace("Открытие сайта завершено");

        //кликнуть по поисковой строке и ввести текст
        WebElement searchbox = driver.findElement(By.id(searchBoxSelector));
        searchbox.click();
        searchbox.sendKeys("отус");

        //кликнуть по кнопке поиска
        waitElementVisible(By.cssSelector(searchButtonSelector)).click();
        waitElementVisible(By.cssSelector(searchFirstResultSelector));

        //сравнить первый найденный результат в выдаче с ожидаемым результатом
        String actualText = driver.findElement(By.cssSelector(searchFirstResultSelector)).getText();
        assertThat(actualText).isEqualTo(expectedText);
    }

    public WebElement waitElementVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
