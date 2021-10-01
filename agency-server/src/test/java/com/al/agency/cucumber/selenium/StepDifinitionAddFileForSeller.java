package com.al.agency.cucumber.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StepDifinitionAddFileForSeller extends SeleniumDriver {

    public int countFileBeforeAdd(){
        List<WebElement> files =
                webDriver.findElements(By.xpath("//*[@id=\"collapse0\"]/div[contains(@class, 'contract')]"));

        return files.size();
    }

    public void choseFile(String filename){
        WebElement fieldInputFile =
                webDriver.findElement(By.xpath("//*[@id=\"heading0\"]//input[@type='file']"));

//        fieldInputFile.sendKeys(); //путь к файлу
    }

    public void clickSaveFile(){
        WebElement buttonSave =
                webDriver.findElement(By.xpath("//*[@id=\"heading0\"]//button[text()='Сохранить']"));

        buttonSave.click();
    }

    public int countFileAfterAdd(){
        List<WebElement> files =
                webDriver.findElements(By.xpath("//*[@id=\"collapse0\"]/div[contains(@class, 'contract')]"));

        return files.size();
    }
}
