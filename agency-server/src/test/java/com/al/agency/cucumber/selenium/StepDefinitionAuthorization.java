package com.al.agency.cucumber.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Component;

@Component
public class StepDefinitionAuthorization extends SeleniumDriver {

    public boolean checkStatus403() throws InterruptedException {
        Thread.sleep(1000);
        try {
            webDriver.findElement(By.xpath("//div[contains(text(), 'status=403')]"));
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }
}
