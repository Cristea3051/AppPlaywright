package com.apptest;

import com.Base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Paths;

class AppPlaywrightTest extends BaseTest {

    @Test
    void testOnChromium() {
        launchBrowser("chromium");
        runTest("chromium");
    }

    @Test
    void testOnFirefox() {
        launchBrowser("firefox");
        runTest("firefox");
    }

    @Test
    void testOnWebkit() {
        launchBrowser("webkit");
        runTest("webkit");
    }
    @Test
    @Description("Test minimal Allure")
    void testMinimal() {
        Allure.step("Pas simplu", () -> System.out.println("Test Allure"));
        assertTrue(true);
    }

    private void runTest(String browserType) {
        page.navigate("https://www.youtube.com/watch?v=kXVL3GUW4to");
        System.out.println(browserType + " title: " + page.title());
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot_" + browserType + "_" + System.currentTimeMillis() + ".png")));
    }
}