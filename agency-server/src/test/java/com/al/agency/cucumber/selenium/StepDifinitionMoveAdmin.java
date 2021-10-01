package com.al.agency.cucumber.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class StepDifinitionMoveAdmin extends SeleniumDriver {
    private final int idUpdateUser = 100;

    public void setNewLoginUser(String login){
        WebElement fieldUserLogin =
                webDriver.findElement(By.xpath("//span[text()='"+ idUpdateUser +"']/following::td[1]/input"));

        fieldUserLogin.sendKeys(Keys.CONTROL + "a");
        fieldUserLogin.sendKeys(login+(byte)(Math.random()*100));
    }

    public void setNotUnicueLoginUser(String login){
        WebElement fieldUserLogin =
                webDriver.findElement(By.xpath("//span[text()='"+ idUpdateUser +"']/following::td[1]/input"));

        fieldUserLogin.sendKeys(Keys.CONTROL + "a");
        fieldUserLogin.sendKeys(login);
    }

    public void clickSaveButton(){
        WebElement buttonSave =
                webDriver.findElement(By.xpath("//span[text()='"+ idUpdateUser +"']/following::td[3]/button"));

        buttonSave.click();
    }

    public String checkStatusUpdate() throws InterruptedException {
        Thread.sleep(3000);
        WebElement labelStatus =
                webDriver.findElement(By.xpath("//div[contains(@class, 'status')]"));

        return labelStatus.getText();
    }
}
