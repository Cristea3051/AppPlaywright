package com.exemplu;

import com.microsoft.playwright.*;

public class WebKitTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            // Inițializare browser WebKit
            Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            // Deschide o pagină
            Page page = browser.newPage();
            // Navighează pe site
            page.navigate("http://playwright.dev/");
            // Așteaptă ca pagina să fie încărcată complet
            page.waitForLoadState();
            // Poți adăuga mai multe acțiuni pe pagină
            System.out.println(page.title());
            // Închide browserul
            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
