package com.exemplu;

import java.util.regex.Pattern;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppPlaywrightTest {
    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void testPlaywrightSite() {
        page.navigate("http://playwright.dev");

        System.out.println("Title: " + page.title());

        // Expect a title "to contain" a substring.
        assertThat(page).hasTitle(Pattern.compile("Playwright"));

        // Create a locator
        Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

        System.out.println("Get Started: " + getStarted.textContent());

        // Expect an attribute "to be strictly equal" to the value.
        assertThat(getStarted).hasAttribute("href", "/docs/intro");

        // Click the get started link.
        getStarted.click();

        // Expects page to have a heading with the name of Installation.
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation"))).isVisible();
    }
}