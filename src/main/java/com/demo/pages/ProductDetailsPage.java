package com.demo.pages;

import com.demo.MenuPage;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductDetailsPage extends MenuPage {
	                   
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
	private MobileElement SBLTitle;

	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
	private MobileElement SBLTxt;
	
	@AndroidFindBy(accessibility = "test-Price")
	private MobileElement SBLPrice;
	
	@AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
	private MobileElement backToProductsBtn;
	
	public String getSBLTitle() {
		return getText(SBLTitle);
	}
	
	public String getSBLTxt() {
		return getText(SBLTxt);
	}
	
	public String getSBLPrice() {
		if(scrollToElement().getText().equals(SBLPrice.getText()));
		return getText(SBLPrice);
	}
	
	public ProductsPage pressBackToProductsBtn() {
		click(backToProductsBtn);
		return new ProductsPage();
	}
	
}
