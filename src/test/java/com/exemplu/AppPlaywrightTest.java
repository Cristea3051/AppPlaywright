package com.exemplu;

import java.util.regex.Pattern;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

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

    @Test
void testOnWebKit() {
    runTest("webkit");
}

    private void runTest(String browserType) {
        Browser browser = null;
        Page page = null;

        try {
            if (browserType.equals("chromium")) {
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            } else if (browserType.equals("firefox")) {
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            } else if (browserType.equals("webkit")) {  // ✅ Adaugă suport pentru WebKit
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(true));
            } else {
                throw new IllegalArgumentException("Browser type not supported: " + browserType);
            }            

            page = browser.newPage();
            page.navigate("http://playwright.dev");

            // Verifică titlul paginii
            System.out.println("[" + browserType.toUpperCase() + "] Title: " + page.title());
            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            // Crează un locator pentru link-ul "Get Started"
            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

            // Verifică atributul href al link-ului
            assertThat(getStarted).hasAttribute("href", "/docs/intro");

            // Fă click pe link-ul "Get Started"
            getStarted.click();

            // Verifică dacă heading-ul "Installation" este vizibil
            assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation"))).isVisible();

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