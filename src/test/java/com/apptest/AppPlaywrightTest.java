package com.apptest;

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
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            }
            else if (browserType.equals("firefox")) {
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
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

            page.getByRole(AriaRole.BUTTON).click();

            page.navigate("http://crm-dash/google-accounts-v2");

            Locator scrollBar = page.locator("revogr-scroll-virtual.horizontal div");

            // Găsește toate header-ele
            Locator headerLocator = page.locator("div[role='columnheader'].rgHeaderCell div");
            int totalColumns = headerLocator.count();
            int currentIndex = 0;

            // Iterează prin fiecare header și derulează până la el
            while (currentIndex < totalColumns) {
                // Derulează scrollbar-ul pe orizontală (incrementează poziția scroll-ului)
                scrollBar.evaluate("element => element.scrollLeft += 200;");

                // Așteaptă câteva momente pentru a da timp aplicației să încarce coloanele vizibile
                page.waitForTimeout(1000);  // Ajustează timpul după nevoile tale

                // Actualizează lista de header-e după fiecare derulare pentru a obține noile coloane vizibile
                List<String> headers = headerLocator.allTextContents();

                // Verifică textul din header-ul curent și adaugă-l în raport
                String headerText = headers.get(currentIndex).trim();
                if (!headerText.isEmpty()) {
                    System.out.println("Header: " + headerText);
                } else {
                    System.out.println("Header is empty");
                }

                // Mergi la următorul element
                currentIndex++;
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