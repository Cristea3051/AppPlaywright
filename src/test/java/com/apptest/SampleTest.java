package com.apptest;

import com.Base.BaseTest;
import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;

public class SampleTest extends BaseTest {

    @Test
    @Description("Testarea paginii Google pe Chromium")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Browser Compatibility")
    void testOnChromium() {
        launchBrowser("chromium");
        runTest("chromium");
    }

    @Test
    @Description("Testarea paginii Google pe Firefox")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Browser Compatibility")
    void testOnFirefox() {
        launchBrowser("firefox");
        runTest("firefox");
    }

    @Test
    @Description("Testarea paginii Google pe Webkit")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Browser Compatibility")
    void testOnWebkit() {
        launchBrowser("webkit");
        runTest("webkit");
    }



    @Step("Rulare test pe browserul {browserType}")
    private void runTest(String browserType) {
        // Navigare la Google
        page.navigate("https://www.google.com/");
        String title = page.title();
        System.out.println(browserType + " title: " + title);

        // Verificare titlu (exemplu de assertion)
        Allure.step("Verifică dacă titlul conține 'Google'", () -> {
            if (!title.contains("Google")) {
                throw new AssertionError("Titlul nu conține 'Google': " + title);
            }
        });

        // Captură screenshot și atașare la Allure
        String screenshotName = "screenshot_" + browserType + "_" + System.currentTimeMillis() + ".png";
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(screenshotName)));
        Allure.addAttachment("Screenshot " + browserType, "image/png", Arrays.toString(screenshot), ".png");
    }
}