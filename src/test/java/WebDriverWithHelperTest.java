import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import java.util.*;
import java.io.*;
import org.openqa.selenium.remote.SessionId;

import java.io.FileReader;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Simple {@link RemoteWebDriver} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>.
 *
 * This test also includes the <a href="https://github.com/saucelabs/sauce-java/tree/master/junit">Sauce JUnit</a> helper classes, which will use the Sauce REST API to mark the Sauce Job as passed/failed.
 *
 * In order to use the {@link SauceOnDemandTestWatcher}, the test must implement the {@link SauceOnDemandSessionIdProvider} interface.
 *
 * @author Ross Rowe
 */
public class WebDriverWithHelperTest implements SauceOnDemandSessionIdProvider {

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("harirao3", "6d0e18c6-2733-4fcf-a010-f71a4f8e1a28");

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    public @Rule
    SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    /**
     * JUnit Rule which will record the test name of the current test.  This is referenced when creating the {@link DesiredCapabilities},
     * so that the Sauce Job is created with the test name.
     */
    public @Rule TestName testName= new TestName();

    private WebDriver driver;

    private String sessionId;

    /**
     * Creates a new {@link RemoteWebDriver} instance to be used to run WebDriver tests using Sauce.
     *
     * @throws Exception thrown if an error occurs constructing the WebDriver
     */
    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("version", "17");
        capabilities.setCapability("platform", Platform.XP);
        capabilities.setCapability("name",  testName.getMethodName());
        this.driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        this.sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Test
    public void webDriverWithHelper() throws Exception {
        String baseUrl="http://puppet.srihari.guru";
        driver.get(baseUrl+ "/dashboard");
        ArrayList<String> accounts = new ArrayList<String>();
        String line =null;
        accounts.add("datasucksalot@gmail.com");
        accounts.add("fucktheData");
        accounts.add("Gmail");
        accounts.add("datasucksalot@gmail.com");
        accounts.add("fucktheData");
        accounts.add("datasuckalot@gmail.com");
        accounts.add("fucktheData");
        accounts.add("datasucksalot@gmail.com");
        accounts.add("fucktheData");

        mainLogin(accounts.get(0), accounts.get(1));
        imap(accounts.get(2), accounts.get(3), accounts.get(4));
        //Need to update CSS selectors for FB/Twitter
        //fb(accounts.get(5), accounts.get(6));
        //twit(accounts.get(7), accounts.get(8));
    }


    public void mainLogin(String name, String pass) {
        String baseUrl="http://puppet.srihari.guru/";

        driver.get(baseUrl);
        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        WebElement btn = driver.findElement(By.cssSelector("input[value=\"Signin\"]"));

        //Enter and submit user info
        username.sendKeys(name);
        password.sendKeys(pass);
        btn.submit();

        if(driver.getCurrentUrl().equals("localhost")){
            System.exit(0);
        }
    }

    public void imap(String host, String name, String pass){
        String baseUrl="http://puppet.srihari.guru/";
        driver.get(baseUrl + "dashboard");
        Select dropHost = new Select(driver.findElement(By.name("host")));
        dropHost.selectByVisibleText(host);

        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        WebElement btn = driver.findElement(By.cssSelector("input[value=\"Link\"]"));

        username.sendKeys(name);
        password.sendKeys(pass);
        btn.submit();

//	     driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        if(driver.findElements(By.cssSelector(".alert.alert-danger")).size() != 0){
            System.out.println(driver.findElement(By.cssSelector(".alert.alert-danger")).getText());
        } else {
            System.out.println(driver.findElement(By.cssSelector(".alert.alert-success")).getText() + " of " + host + " account");
        }

    }

    public void fb(String name, String pass){
        String baseUrl="http://puppet.srihari.guru/";
        driver.get(baseUrl + "dashboard");

        WebElement fbBtn = driver.findElement(By.cssSelector("a[href=\"/auth/facebook\"]"));
        fbBtn.click();

        WebElement username = driver.findElement(By.cssSelector("input[name=\"username\"]"));
        WebElement password = driver.findElement(By.cssSelector("input[name=\"password\"]"));
        WebElement loginbtn  = driver.findElement(By.cssSelector("input[value=\"Signin\"]"));
        username.sendKeys(name);
        password.sendKeys(pass);
        loginbtn.click();

        if(driver.findElements(By.cssSelector("div[class=\"pam login_error_box uiBoxRed\"]")).size() != 0){
            System.out.println(driver.findElement(By.cssSelector("div[class=\"pam login_error_box uiBoxRed\"]")).getText());
        } else {
            WebElement okay = driver.findElement(By.cssSelector("button[name=\"__CONFIRM__\"]"));
            okay.submit();
            System.out.println("Success!");
        }

    }

    public void twit(String name, String pass){
        String baseUrl="http://puppet.srihari.guru/";
        driver.get(baseUrl + "dashboard");
        WebElement twitBtn = driver.findElement(By.cssSelector("a[href=\"/auth/twitter\"]"));

        twitBtn.click();



        WebElement username = driver.findElement(By.cssSelector("input[class=\"text\"]"));
        WebElement password = driver.findElement(By.cssSelector("input[class=\"password text\"]"));
        WebElement signin  = driver.findElement(By.cssSelector("input[class=\"submit button selected\"]"));
        username.sendKeys(name);
        password.sendKeys(pass);
        signin.submit();

        if(driver.findElements(By.cssSelector("span[class=\"message-text\"]")).size() != 0){
            System.out.println(driver.findElement(By.cssSelector("span[class=\"message-text\"]")).getText());
        } else {
            WebElement authorize = driver.findElement(By.cssSelector("input[value=\"Authorize app\"]"));
            authorize.click();
            System.out.println("Success!");
        }
//        String text = driver.findElement(By.cssSelector("span[class=\"message-text\"]")).getText();
//        System.out.println(text);

    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
