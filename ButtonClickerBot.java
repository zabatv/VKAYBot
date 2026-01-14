import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ButtonClickerBot {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "D://chromedriver-win64/chromedriver.exe");
        WebDriver driver = null;
        try {

            driver = new ChromeDriver();
            login(driver);
            openAndWaitForPageLoad(driver, "https://example.com");
            openAndWaitForPageLoad(driver, "https://vktarget.ru/list");

        }catch (Exception e){}
        while (true){
            try {
                Thread.sleep(1000);

            while (true) {
                try {clickButtonByText(driver, "канал");}catch (Exception e){}
                if (ButtonClickerBot.isDomainOpenInAnyTab(driver, "youtube.com")) {
                    for (int i = 0; i < 5; i++) {

                        ButtonClickerBot.waitForPageLoad(driver);
                        Thread.sleep(1000);
                        clickButtonByText(driver, "Подписаться");
                        System.out.println("&&&");
                        Thread.sleep(1000);
                    }
                }




                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}

    }

    private static void openAndWaitForPageLoad(WebDriver driver, String url) {
        try {
            driver.get(url.trim());
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            System.out.println("Страница успешно загружена: " + url);
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке страницы: " + e.getMessage());
            throw e;         }
    }

    public static void clickButtonByText(WebDriver driver, String text) {
        try {
            WebElement button = ButtonClickerBot.findButtonByText(driver, text);

            System.out.println("Кнопка найдена! Нажимаем...");
            button.click();
            System.out.println("Кнопка успешно нажата!");

        } catch (Exception e) {
            System.err.println("Ошибка при поиске или нажатии на кнопку: " + e.getMessage());

        }
    }

    static WebElement findButtonByText(WebDriver driver, String text) {
        try {
            for (WebElement button : driver.findElements(By.tagName("button"))) {
                if (button.getText().trim().equalsIgnoreCase(text)) {
                    return button;
                }
            }


            for (WebElement element : driver.findElements(By.xpath("//*[contains(text(), '" + text + "')]"))) {
                return element;
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске кнопки: " + e.getMessage());
        }
        return null;
    }

    static boolean isDomainOpenInAnyTab(WebDriver driver, String domain) {
        String originalWindow = driver.getWindowHandle();
        try {
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                if (driver.getCurrentUrl().contains(domain)) {
                    System.out.println("Домен '" + domain + "' найден в одной из вкладок.");
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при проверке домена: " + e.getMessage());
        }
        System.out.println("Домен '" + domain + "' не найден ни в одной из вкладок.");
        return false;
    }



    private static void openChannelByText(WebDriver driver, String text) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement textElement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(text(), '" + text + "')]")));

            if (textElement != null && textElement.isDisplayed() && textElement.isEnabled()) {
                System.out.println("Текст '" + text + "' найден. Нажимаем...");
                textElement.click();
                System.out.println("Переход по тексту выполнен.");
            } else {
                System.out.println("Текст '" + text + "' не найден или недоступен.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске текста '" + text + "': " + e.getMessage());
            throw e;
        }
    }

    public static boolean isTextPresentOnPage(WebDriver driver, String text) {
        try {
            // Используем XPath для поиска текста на странице
            List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + text + "')]"));

            // Если список элементов не пустой, значит текст найден
            if (!elements.isEmpty()) {
                System.out.println("Текст '" + text + "' найден на странице.");
                return true;
            } else {
                System.out.println("Текст '" + text + "' не найден на странице.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка при проверке наличия текста на странице: " + e.getMessage());
            return false;
        }
    }

    private static void login(WebDriver driver) throws InterruptedException {
        openAndWaitForPageLoad(driver, "https://youtube.com/");
        waitForUserInput();
        openAndWaitForPageLoad(driver, "https://vktarget.ru/");
        loginWithCredentials(driver);
        clickButtonByText(driver, "Войти");
        waitForPageLoad(driver);
    }

    private static void loginWithCredentials(WebDriver driver, String email, String password) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@placeholder='Email' or @name='email' or @id='email']")));
            emailField.clear();
            emailField.sendKeys(email);
            System.out.println("Email: " + email);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@placeholder='Пароль' or @name='password' or @id='password']")));
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("Пароль");
        } catch (Exception e) {
            System.err.println("Ошибка при вводе логина и пароля" + e.getMessage());
            throw e;
        }
    }

    static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
    private static void waitForUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Программа приостановлена. Введите 'c', чтобы продолжить...");


        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("c")) {
                System.out.println("Получен сигнал к продолжению.");
                break;
            } else {
                System.out.println("Ожидался ввод 'c'. Попробуйте снова.");
            }
        }
    }
}
