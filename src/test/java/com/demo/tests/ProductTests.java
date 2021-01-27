package com.demo.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.demo.BaseTest;
import com.demo.pages.LoginPage;
import com.demo.pages.ProductDetailsPage;
import com.demo.pages.ProductsPage;
import com.demo.pages.SettingsPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class ProductTests extends BaseTest {
	
  LoginPage loginPage;
  ProductsPage productsPage;
  SettingsPage settingsPage;
  ProductDetailsPage productDetailsPage;
  InputStream datais;
  JSONObject loginUsers;
  
  @BeforeClass
  public void beforeClass() throws IOException {	  
	  try {
		String dataFileName = "data/loginUsers.json";
		datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
		JSONTokener tokener = new JSONTokener(datais);
		loginUsers = new JSONObject(tokener);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (datais != null) {
			datais.close();
		}
	}
	closeApp();
	launchApp();
  }

  @AfterClass
  public void afterClass() {
	  
  }
  
  @BeforeMethod
  public void beforeMethod(Method m) {
	  loginPage = new LoginPage();
	  System.out.println("\n"+ "***starting test: "+ m.getName()+"***"+"\n");
	  productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"), 
			  loginUsers.getJSONObject("validUser").getString("password"));

  }

  @AfterMethod
  public void afterMethod() {
	  settingsPage = productsPage.pressSettingsBtn();
	  loginPage = settingsPage.pressLogoutBtn();
  }

  @Test
  public void validateProductsPage() {
	  SoftAssert sa = new SoftAssert();
	  
	  String actualTitle = productsPage.getSBLTitle();
	  sa.assertEquals(actualTitle, strings.get("products_page_slb_title"));
	  
	  String actualPrice = productsPage.getSBLPrice();
	  sa.assertEquals(actualPrice, strings.get("products_page_slb_price"));
	    
	  sa.assertAll();
  }
  
  @Test
  public void validateProductsDetailPage() {
	  SoftAssert sa = new SoftAssert();
	  productDetailsPage = productsPage.pressSBLTitle();
	  
	  String actualSBLTitle = productDetailsPage.getSBLTitle();
	  sa.assertEquals(actualSBLTitle, strings.get("product_details_page_slb_title"));
	  
	  String actualSBLPrice = productDetailsPage.getSBLPrice();
	  System.out.println("actualSBLPrice: " + actualSBLPrice);
	  sa.assertEquals(actualSBLPrice, strings.get("product_details_page_slb_price"));
	  System.out.println("ExpectedSBLPrice: " + strings.get("product_details_page_slb_price"));
	  
	  productsPage = productDetailsPage.pressBackToProductsBtn();
	  	
	  sa.assertAll();
  }
  
}
