import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CookieManager {

    private static final String COOKIES_VKTARGET_FILE_PATH = "cookies_vktarget.txt";
    private static final String COOKIES_YOUTUBE_FILE_PATH = "cookies_youtube.txt";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "D://chromedriver-win64/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {

            handleDomain(driver, "https://vktarget.ru/", COOKIES_VKTARGET_FILE_PATH);

            handleDomain(driver, "https://www.youtube.com/", COOKIES_YOUTUBE_FILE_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void handleDomain(WebDriver driver, String url, String cookiesFilePath) throws InterruptedException {

        driver.get(url);

        if (areCookiesSaved(cookiesFilePath)) {
            loadCookies(driver, cookiesFilePath);
            driver.navigate().refresh(); // Обновляем страницу, чтобы применить куки
            System.out.println("Куки для " + url + " загружены.");
        } else {
            performManualLogin(driver, url);
            saveCookies(driver, cookiesFilePath);
            System.out.println("Куки для " + url + " сохранены.");
        }

        if (driver.getCurrentUrl().contains(url)) {
            System.out.println("Успешный вход в аккаунт на " + url);
        }
    }


    private static void performManualLogin(WebDriver driver, String url) throws InterruptedException {
        System.out.println("Выполняется ручной вход на " + url + ". Войдите в аккаунт вручную...");
        Thread.sleep(300000); // Даем пользователю время войти в аккаунт
    }


    private static void saveCookies(WebDriver driver, String cookiesFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cookiesFilePath))) {
            for (Cookie cookie : driver.manage().getCookies()) {
                String expiry = (cookie.getExpiry() != null) ? cookie.getExpiry().toString() : "null";
                writer.write(cookie.getName() + ";" + cookie.getValue() + ";" + cookie.getDomain() + ";" +
                        cookie.getPath() + ";" + expiry + ";" + cookie.isSecure());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении куков: " + e.getMessage());
        }
    }


    private static void loadCookies(WebDriver driver, String cookiesFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cookiesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cookieData = line.split(";");
                if (cookieData.length >= 6) {
                    String name = cookieData[0];
                    String value = cookieData[1];
                    String domain = cookieData[2];
                    String path = cookieData[3];
                    Date expiry = parseExpiryDate(cookieData[4]);
                    boolean isSecure = Boolean.parseBoolean(cookieData[5]);

                    if (!driver.getCurrentUrl().contains(domain)) {
                        System.out.println("Куки для домена " + domain + " не могут быть добавлены, так как текущий URL не содержит этот домен.");
                        continue;
                    }

                    Cookie cookie = new Cookie.Builder(name, value)
                            .domain(domain)
                            .path(path)
                            .expiresOn(expiry)
                            .isSecure(isSecure)
                            .build();

                    driver.manage().addCookie(cookie);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке куков: " + e.getMessage());
        }
    }


    private static Date parseExpiryDate(String expiryString) {
        if (expiryString == null || expiryString.isEmpty() || expiryString.equals("null")) {
            return null; // Если дата истечения отсутствует
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow")); // Указываем часовой пояс
            return dateFormat.parse(expiryString);
        } catch (ParseException e) {
            System.err.println("Ошибка при парсинге даты истечения куки: " + expiryString);
            return null;
        }
    }


    private static boolean areCookiesSaved(String cookiesFilePath) {
        File file = new File(cookiesFilePath);
        return file.exists() && file.length() > 0;
    }
}
