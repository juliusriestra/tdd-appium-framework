package com.demo.pages;

import com.demo.BaseTest;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class LoginPage extends BaseTest {
	
	@AndroidFindBy(accessibility = "test-Username")
	@iOSXCUITFindBy(id = "test-Username")
	private MobileElement usernameTxtField;
	
	@AndroidFindBy(accessibility = "test-Password")
	@iOSXCUITFindBy(id = "test-Password")
	private MobileElement passwordTxtField;
	
	@AndroidFindBy(accessibility = "test-LOGIN")
	@iOSXCUITFindBy(id = "test-LOGIN")
	private MobileElement loginBtn;
 
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
	@iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")
	private MobileElement errTxt;
    
	public LoginPage enterUserName(String username) {
		sendKeys(usernameTxtField, username);
		return this;
	}
	
	public LoginPage enterPassword(String password) {
		sendKeys(passwordTxtField, password);
		return this;
	}
	
	public ProductsPage pressLoginButton() {
		click(loginBtn);
		return new ProductsPage();
	}
	
	public ProductsPage login(String username, String password) {
		enterUserName(username);
		enterPassword(password);	
		return pressLoginButton();
	}
	
	public String getErrMessage() {
		return getText(errTxt);
	}
}
