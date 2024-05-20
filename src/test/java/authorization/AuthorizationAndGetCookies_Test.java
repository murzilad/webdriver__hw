package authorization;

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

public class AuthorizationAndGetCookies_Test {

    private String username = System.getProperty("username");
    private String password = System.getProperty("password");

    private final static Logger logger = LogManager.getLogger(AuthorizationAndGetCookies_Test.class);
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


    public WebElement waitElementVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private boolean waitElementNotVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Test
    public void authorizationAndGetCookies() {

        final By enterButtonLocator = By.xpath("//button[text() = 'Войти']");
        final By emailFieldLocator = By.xpath("//div[./input[@name='email']]");
        final By emailInputSelector = By.cssSelector("input[name = 'email']");
        final By passwordFieldLocator = By.xpath("//div[./input[@type='password']]");
        final By passwordInputSelector = By.cssSelector("input[type = 'password']");
        final By submitButtonLocator = By.xpath("//button[./*[text()='Войти']]");

        logger.trace("Открытие браузера начато");
        driver = new ChromeDriver();
        logger.trace("Открытие браузера завершено");

        //открыть Chrome в режиме полного экрана
        driver.manage().window().maximize();

        logger.trace("Открытие сайта начато");
        driver.get("https://otus.ru");
        logger.trace("Открытие сайта завершено");

        //клик по кнопке войти
        waitElementVisible(enterButtonLocator).click();

        //в поле email ввести адрес эл почты
        waitElementVisible(emailFieldLocator).click();
        driver.findElement(emailInputSelector).sendKeys(username);

        //в поле password ввести пароль
        waitElementVisible(passwordFieldLocator).click();
        driver.findElement(passwordInputSelector).sendKeys(password);

        //клик на кнопку войти
        waitElementVisible(submitButtonLocator).click();

        //после авторизации кнопка войти не отображается
        boolean enterButtonNotVisible = waitElementNotVisible(enterButtonLocator);
        logger.info(enterButtonNotVisible);

        //вывести все куки в лог
        logger.trace(driver.manage().getCookies());

        assertThat(enterButtonNotVisible)
                .as("Error: item displays in DOM")
                .isTrue();

    }
}
