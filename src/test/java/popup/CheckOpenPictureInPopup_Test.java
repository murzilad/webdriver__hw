package popup;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckOpenPictureInPopup_Test {

    private final static Logger logger = LogManager.getLogger(CheckOpenPictureInPopup_Test.class);
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
    public void checkOpenPictureInPopup() {

        String popupSelector = ".pp_pic_holder";
        String pictureSelector = "li[data-id = id-2]";

        logger.trace("Открытие браузера начато");
        driver = new ChromeDriver();
        logger.trace("Открытие браузера завершено");

        logger.trace("Открытие сайта начато");
        driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");
        logger.trace("Открытие сайта завершено");

        //открыть Chrome в режиме киоска
        driver.manage().window().fullscreen();

        //ожидание загрузки картинок на странице
        driver.findElement(By.cssSelector(pictureSelector));

        //проверка отсутствия попапа на странице
        boolean popupNotVisible = waitElementNotVisible(By.cssSelector(popupSelector));
        logger.info(popupNotVisible);

        //клик по картинке
        WebElement image = driver.findElement(By.cssSelector(pictureSelector));
        image.click();

        //проверка открытия картинки в модальном окне
        assertThat(waitElementVisible(By.cssSelector(popupSelector)).isDisplayed())
                .as("Error: item displays in DOM")
                .isTrue();

    }

    public WebElement waitElementVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean waitElementNotVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}

