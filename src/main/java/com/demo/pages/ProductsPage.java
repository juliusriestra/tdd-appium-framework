package com.demo.pages;

import com.demo.BaseTest;
import com.demo.MenuPage;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage {
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]/preceding-sibling::android.view.ViewGroup/android.widget.TextView")
	private MobileElement productTitleTxt;
	
	@AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
	private MobileElement SBLTitle;
	
	@AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
	private MobileElement SBLPrice;
	
//	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
//	private MobileElement settingBtn;
	
	public String getTitle() {
		return getText(productTitleTxt);
	}
	
	public String getSBLTitle() {
		return getText(SBLTitle);
	}
	
	public String getSBLPrice() {
		return getText(SBLPrice);
	}
	
	public ProductDetailsPage pressSBLTitle() {
		click(SBLTitle);
		return new ProductDetailsPage();
	}
	
//	public SettingsPage pressSettingsBtn() {
//		click(settingBtn);
//		return new SettingsPage();
//	}
}