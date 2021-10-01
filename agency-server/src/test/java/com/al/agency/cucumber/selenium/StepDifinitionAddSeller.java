package com.al.agency.cucumber.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class StepDifinitionAddSeller extends SeleniumDriver {


    public void inputParamSeller(){
        WebElement fieldSeller =
                webDriver.findElement(By.xpath("//*[@id=\"fio\"]"));
        fieldSeller.sendKeys("Иванов Иван Иванович");

        long numberPassport = ((long)(Math.random()*1e9)) + 1_000_000_000L;

        webDriver
                .findElement(By.xpath("//*[@id=\"passport\"]"))
                .sendKeys(String.valueOf(numberPassport));

        webDriver
                .findElement(By.xpath("//*[@id=\"phone\"]"))
                .sendKeys("+78561548731");
    }

    public void clickSaveButton(){
        WebElement buttonSave =
                webDriver.findElement(By.xpath("//*[attribute::type='submit']"));

        buttonSave.click();
    }

    public String getCurrentUrl(){
        String url= webDriver.getCurrentUrl().replaceAll("http://", "");
        int payloadBegin=url.indexOf("/");
        int payloadEnd=url.indexOf("?");
        return url.substring(payloadBegin, payloadEnd!=-1?payloadEnd:url.length());
    }

}
