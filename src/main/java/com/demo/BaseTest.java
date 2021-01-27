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
import io.appium.java_client.screenrecording.CanRecordScreen;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
   
  protected static AppiumDriver driver;
  protected static Properties props;
  protected static HashMap<String, String> strings = new HashMap<String, String>();
  protected static String platform;
  protected static String datetime;
  protected static FileOutputStream stream;
  InputStream inputStream;
  InputStream stringsis;
  TestUtils utils;
  
  public BaseTest() {
	PageFactory.initElements(new AppiumFieldDecorator(driver), this);
  }
  
  @BeforeMethod
  public void beforeMethod() {
	  System.out.println("Super @BeforeMethod");
	((CanRecordScreen) driver).startRecordingScreen();
  }
  
  @AfterMethod
  public synchronized void afterMethod(ITestResult result) throws Exception {
	  System.out.println("Super @AfterMethod");
	  String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
		
	  Map <String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();		
	  String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName") 
	  + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();
		
	  File videoDir = new File(dirPath);
		
	  synchronized(videoDir){
		if(!videoDir.exists()) {
			videoDir.mkdirs();
	   }	
	   }
	   FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
			stream.write(Base64.decodeBase64(media));
			stream.close();
//			utils.log().info("video path: " + videoDir + File.separator + result.getName() + ".mp4");
		} catch (Exception e) {
//			utils.log().error("error during video capture" + e.toString());
		} finally {
			if(stream != null) {
				stream.close();
			}
		}		

  }

  @Parameters({"emulator", "platformName", "platformVersion", "deviceName", "udid"})
  @BeforeTest
  public void beforeTest(String emulator, String platformName, String platformVersion, String deviceName, String udid) throws Exception {
	  utils = new TestUtils();
	  datetime = utils.dateTime();
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

  public String getDateTime() {
	return datetime;  
  } 
  
  public AppiumDriver getDriver() {
	  return driver;
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
