package com.apptest;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import com.microsoft.playwright.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
            }
            else if (browserType.equals("firefox")) {
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            }
            else {
                throw new IllegalArgumentException("Browser type not supported: " + browserType);
            }            

            page = browser.newPage();
            page.navigate("http://crm-dash/login");

            Locator emailAddress = page.locator("#login-username");
            emailAddress.fill("victor.cristea@vebo.io");
            assertEquals("victor.cristea@vebo.io", emailAddress.inputValue(), "Email field is incorrect!");

            Locator password = page.locator("#login-password");
            password.fill("j8L3pc5hJ20Sjn10Lp!");
            assertEquals("j8L3pc5hJ20Sjn10Lp!", password.inputValue(), "password field is incorrect!");
            Locator loginButton = page.locator("button:has-text('Login')");
            loginButton.click();

            page.navigate("http://crm-dash/google-accounts-v2");
            page.waitForTimeout(1000);

            Locator scrollBar = page.locator("revogr-scroll-virtual.horizontal.hydrated");
            Locator headerLocator = page.locator("div.rgHeaderCell div.header-content");

            Set<String> foundHeaders = new HashSet<>();
            Number initialScroll = (Number) scrollBar.evaluate("element => element.scrollLeft");
            double initialScrollValue = initialScroll.doubleValue();

            boolean canScroll = true;
            while (canScroll) {

                List<String> headers = headerLocator.allTextContents();
                for (String header : headers) {
                    String cleanHeader = header.trim();
                    if (!cleanHeader.isEmpty() && !foundHeaders.contains(cleanHeader)) {
                        foundHeaders.add(cleanHeader);
                        System.out.println("Header găsit: " + cleanHeader);
                    }
                }

                Number prevScroll = (Number) scrollBar.evaluate("element => element.scrollLeft");
                double prevScrollValue = prevScroll.doubleValue();

                scrollBar.evaluate("element => element.scrollLeft += 300");

                page.waitForTimeout(1000);

                Number newScroll = (Number) scrollBar.evaluate("element => element.scrollLeft");
                double newScrollValue = newScroll.doubleValue();

                if (newScrollValue == prevScrollValue) {
                    canScroll = false;
                }
            }

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