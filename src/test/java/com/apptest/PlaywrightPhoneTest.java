package com.apptest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ColorScheme;

import java.nio.file.Paths;

public class PlaywrightPhoneTest{
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setDeviceScaleFactor(3)
                    .setViewportSize(1170, 2532)
                    .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1")
                    .setGeolocation(47.0105, 28.8638) // Chișinău, Moldova
                    .setPermissions(java.util.Arrays.asList("geolocation"))
                    .setColorScheme(ColorScheme.DARK)
                    .setIsMobile(true)
                    .setHasTouch(true));

            Page page = context.newPage();
            page.navigate("https://www.google.com");
            page.waitForTimeout(15000); // 15 secunde să treci de CAPTCHA
            page.locator("[name='q']").fill("Wikipedia");
            page.locator("[name='q']").press("Enter");
            page.waitForTimeout(5000);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("google_search_iphone_moldova.png")));

            browser.close();
        }
    }
}