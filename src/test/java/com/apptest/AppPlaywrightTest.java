package com.apptest;

import java.util.regex.Pattern;
import com.microsoft.playwright.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)  // 🔥 Permite rularea în paralel a testelor
public class AppPlaywrightTest {
    private static Playwright playwright;

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
    }

    @AfterAll
    static void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void testOnChromium() {
        runTest("chromium");
    }

    @Test
    void testOnFirefox() {
        runTest("firefox");
    }


    private void runTest(String browserType) {
        Browser browser = null;
        Page page = null;

        try {
            if (browserType.equals("chromium")) {
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            } else if (browserType.equals("firefox")) {
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            } else {
                throw new IllegalArgumentException("Browser type not supported: " + browserType);
            }            

            page = browser.newPage();
            page.navigate("http://crm-dash/login");

            // Verifică titlul paginii
            System.out.println("[" + browserType.toUpperCase() + "] Title: " + page.title());
            assertThat(page).hasTitle(Pattern.compile("iCRM v.38.46"));

            String text = page.locator("text=Welcome, please login.").textContent();
            System.out.println("Text găsit: " + text);

            Locator emailAddress = page.locator("#login-username");
            emailAddress.fill("victor.cristea@vebo.io");

            // Asertie directă, fără if
            assertEquals("victor.cristea@vebo.io", emailAddress.inputValue(), "Email field is incorrect!");

        } finally {
            if (page != null) {
                page.close();
            }
            if (browser != null) {
                browser.close();
            }
        }
    }
}