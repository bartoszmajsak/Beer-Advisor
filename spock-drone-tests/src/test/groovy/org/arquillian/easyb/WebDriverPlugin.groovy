package org.arquillian.easyb

import org.easyb.plugin.BasePlugin
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver

/**
 * Tiny easyb plugin to manage WebDriver initialization and cleanup transparently
 * in the scenario.
 *
 * @see BeerAdvisorSearch.story
 *
 */
class WebDriverPlugin extends BasePlugin {

    String type

    public String getName() {
        return "WebDriver";
    }

    def beforeStory(Binding binding) {
        binding.driver = resolveDriver()
    }

    def configure(Closure closure) {
        closure.call(this)
    }

    def afterStory(Binding binding) {
        binding.driver?.quit()
    }

    def WebDriver resolveDriver() {
        switch (type?.toLowerCase()) {
            case null: return new ChromeDriver()
            case "chrome": return new ChromeDriver()
            case "firefox": return new FirefoxDriver()
            case "ie": return new InternetExplorerDriver()
            default: throw new RuntimeException("""Unsupported driver type. Try ["ie", "firefox", "chrome"]""")
        }
    }
}