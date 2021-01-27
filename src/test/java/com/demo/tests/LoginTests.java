package com.demo.tests;

import org.testng.annotations.Test;

import com.demo.BaseTest;
import com.demo.pages.LoginPage;
import com.demo.pages.ProductsPage;

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

public class LoginTests extends BaseTest {
	
  LoginPage loginPage;
  ProductsPage productsPage;
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
	  System.out.println("LoginTest @BeforeMethod");
	  loginPage = new LoginPage();
	  System.out.println("\n"+ "***starting test: "+ m.getName()+"***"+"\n");
  }

  @AfterMethod
  public void afterMethod() {
	  System.out.println("LoginTest @AfterMethod");
  }

  @Test
  public void invalidUserName() { 
	  loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
	  loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
	  loginPage.pressLoginButton();
	  String actualError = loginPage.getErrMessage();
	  String expectedError = strings.get("err_invalid_username_or_password");
	  System.out.println("actualError"+ actualError+ "expectedError"+ expectedError);
	  Assert.assertEquals(actualError, expectedError);
  }
  
  @Test
  public void invalidPassword() {
	  loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
	  loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
	  loginPage.pressLoginButton();
	  String actualError = loginPage.getErrMessage();
	  String expectedError = strings.get("err_invalid_username_or_password");
	  System.out.println("actualError"+ actualError+ "expectedError"+ expectedError);
	  Assert.assertEquals(actualError, expectedError);
	  
  }
  
  @Test
  public void successLogin() {
	  loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
	  loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
	  productsPage = loginPage.pressLoginButton();
	  String actualSuccess = productsPage.getTitle();
	  String expectedSuccess = strings.get("product_title");
	  System.out.println("actualSuccess"+ actualSuccess+ "expectedSuccess"+ expectedSuccess);
	  Assert.assertEquals(actualSuccess, expectedSuccess);
  }
}
