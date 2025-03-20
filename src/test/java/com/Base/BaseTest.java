package com.Base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Paths;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
    }

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
    }

    @AfterEach
    void tearDown() {
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
}