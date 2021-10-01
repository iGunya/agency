package com.al.agency.cucumber.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeleniumDriver {
    private final String URL_APP = "http://localhost:2203";

    @Autowired
    protected WebDriver webDriver;

    public void clearCookie(){
        webDriver.manage().deleteAllCookies();
    }

    public void getPageByURL(String url) throws InterruptedException {
        webDriver.get(URL_APP + url);
    }

    public void setLoginOnPageLogin(String login){
        WebElement fieldLogin =
                webDriver.findElement(By.xpath("//*[@id=\"username\"]"));
        fieldLogin.sendKeys(login);
    }

    public void setPasswordOnPageLogin(String password){
        WebElement fieldPassword =
                webDriver.findElement(By.xpath("//*[@id=\"password\"]"));
        fieldPassword.sendKeys(password);
    }

    public void clickButtonLoginOnPageLogin(){
        WebElement buttonLogIn =
                webDriver.findElement(By.xpath("//*[attribute::type='submit']"));
        buttonLogIn.click();
    }

    public void close(){
        webDriver.close();
    }
}