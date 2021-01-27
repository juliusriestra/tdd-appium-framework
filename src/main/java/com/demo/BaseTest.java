package com.demo;

import org.testng.annotations.Test;

import com.demo.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;

public class BaseTest {
   
  protected static AppiumDriver driver;
  protected static Properties props;
  protected static HashMap<String, String> strings = new HashMap<String, String>();
  protected static String platform;
  InputStream inputStream;
  InputStream stringsis;
  TestUtils utils;
  
  public BaseTest() {
	PageFactory.initElements(new AppiumFieldDecorator(driver), this);
  }
  
  @Parameters({"emulator", "platformName", "platformVersion", "deviceName", "udid"})
  @BeforeTest
  public void beforeTest(String emulator, String platformName, String platformVersion, String deviceName, String udid) throws Exception {
	  platform = platformName;
	  URL url;
	  try {
		  props = new Properties();
		  String propFileName = "config.properties";
		  String xmlFileName = "strings/strings.xml";
		  
		  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		  props.load(inputStream);
		  
		  stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
		  utils = new TestUtils();
		  strings = utils.parseStringXML(stringsis);
		  
		  DesiredCapabilities caps = new DesiredCapabilities();
		  caps.setCapability("deviceName", deviceName);
		  caps.setCapability("platformName", platformName);
		  
		  switch (platformName) {
			case "Android":
				caps.setCapability("automationName", props.getProperty("androidAutomationName"));
				caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
				caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
				URL androidAppUrl = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
				if (emulator.equalsIgnoreCase("true")) {
				    caps.setCapability("platformVersion", platformVersion);
					caps.setCapability("avd", deviceName);
				}
				else
				{
					caps.setCapability("udid", udid);
				}
//				String appUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
//				System.out.println("appURL is:"+ appUrl);
				caps.setCapability("app", androidAppUrl);
				url = new URL(props.getProperty("appiumURL"));
					
				driver = new AndroidDriver<MobileElement>(url, caps);
				break;
			case "iOS":
                caps.setCapability("automationName", props.getProperty("androidAutomationName"));
                caps.setCapability("platformVersion", platformVersion);
                URL iOSAppUrl = getClass().getClassLoader().getResource(props.getProperty("iOSAppLocation"));
//              String appUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
//				System.out.println("appURL is:"+ appUrl);
                caps.setCapability("app", iOSAppUrl);
                url = new URL(props.getProperty("iOSURL"));
                driver = new IOSDriver(url, caps);
			default:
			break;
		}
		  
		  String sessionId = driver.getSessionId().toString();
		 
	} catch (Exception e) {
		e.printStackTrace();
		throw e;
	} finally {
		if (inputStream != null) {
			inputStream.close();
		}
		if (stringsis != null) {
			stringsis.close();
		}
	}
	  
  }

  @AfterTest
  public void afterTest() {
	  driver.quit();
  }

  public void waitForVisibility(MobileElement e) {
		WebDriverWait wait = new WebDriverWait(driver, TestUtils.WAIT);
		wait.until(ExpectedConditions.visibilityOf(e));
  }
  
  public void clear(MobileElement e) {
	  waitForVisibility(e);
	  e.clear();
  }
  
  public void click(MobileElement e) {
      waitForVisibility(e);
      e.click();
  }
  
  public void sendKeys(MobileElement e, String txt) {
		waitForVisibility(e);
		e.sendKeys(txt);
  }
  
  public String getAttribute(MobileElement e, String attribute) {
      waitForVisibility(e);
      return e.getAttribute(attribute);
  }
  
  public String getText(MobileElement e) {
	  switch (platform) {
	case "Android":
		return getAttribute(e, "text");
	case "iOS":
		return getAttribute(e, "label");
	}
	return null;
  }
  
  public void closeApp() {
	  ((InteractsWithApps) driver).closeApp();
  }
  
  public void launchApp() {
	  ((InteractsWithApps) driver).launchApp();
  }
  
  public MobileElement scrollToElement() {	  
		return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));");
  }  
}
