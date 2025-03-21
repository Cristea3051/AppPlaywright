package com.Base;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@ExtendWith(BaseTest.TestResultWatcher.class)
public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    private boolean testFailed = false;

    @BeforeEach
    @Step("Inițializare Playwright și browser")
    void setUp() {
        playwright = Playwright.create();
    }

    @Step("Lansare browser: {browserType}")
    protected void launchBrowser(String browserType) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(true);
        if ("chromium".equalsIgnoreCase(browserType)) {
            browser = playwright.chromium().launch(launchOptions);
        } else if ("firefox".equalsIgnoreCase(browserType)) {
            browser = playwright.firefox().launch(launchOptions);
        } else if ("webkit".equalsIgnoreCase(browserType)) {
            browser = playwright.webkit().launch(launchOptions);
        } else {
            throw new IllegalArgumentException("Browser type not supported: " + browserType);
        }
        page = browser.newPage();

        page.onRequest(request -> Allure.addAttachment("Request: " + request.url(), request.method()));
        page.onResponse(response -> Allure.addAttachment("Response: " + response.url(), String.valueOf(response.status())));
    }

    @AfterEach
    @Step("Închidere browser și Playwright")
    void tearDown() {
        if (testFailed) {
            captureScreenshotOnFailure();
        }
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    protected void captureScreenshotOnFailure() {
        if (page != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String screenshotPath = "screenshots/failure_" + timestamp + ".png";
            try {
                Files.createDirectories(Paths.get("screenshots"));
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
                Allure.addAttachment("Screenshot la eșec", "image/png", Arrays.toString(screenshot), ".png");
            } catch (IOException e) {
                System.err.println("Eroare la salvarea screenshot-ului: " + e.getMessage());
            }
        }
    }

    static class TestResultWatcher implements TestWatcher {
        @Override
        public void testFailed(org.junit.jupiter.api.extension.ExtensionContext context, Throwable cause) {
            BaseTest baseTest = (BaseTest) context.getRequiredTestInstance();
            baseTest.testFailed = true;
        }

        @Override
        public void testSuccessful(org.junit.jupiter.api.extension.ExtensionContext context) {
            BaseTest baseTest = (BaseTest) context.getRequiredTestInstance();
            baseTest.testFailed = false;
        }

        @Override
        public void testAborted(org.junit.jupiter.api.extension.ExtensionContext context, Throwable cause) {}
        @Override
        public void testDisabled(org.junit.jupiter.api.extension.ExtensionContext context, Optional<String> reason) {}
    }
}