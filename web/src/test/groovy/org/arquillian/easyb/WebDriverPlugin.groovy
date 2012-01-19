package org.arquillian.easyb

import org.easyb.plugin.BasePlugin
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.events.EventFiringWebDriver

class WebDriverPlugin extends BasePlugin {

    private WebDriver driver;

    public String getName() {
        return "WebDriver";
    }

    def beforeStory(Binding binding) {
        driver = new FirefoxDriver()
        binding.setVariable("driver", driver)
    }

    def afterStory(Binding binding) {
        driver.quit()
    }

}