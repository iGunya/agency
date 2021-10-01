package com.al.agency.cucumber.steps;

import com.al.agency.cucumber.selenium.StepDifinitionAuth;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;


public class AuthStep{
    @Autowired
    private StepDifinitionAuth stepDifinitionAuth;

    @Пусть("я захожу на страницу аунтефикации {string}")
    public void яЗахожуНаСтраницуАунтефикации(String url) throws InterruptedException {
        stepDifinitionAuth.getPageByURL(url);
    }

    @Когда("я ввожу логин {string}")
    public void яВвожуЛогин(String login) {
        stepDifinitionAuth.setLoginOnPageLogin(login);
    }

    @И("я ввожу пароль {string}")
    public void яВвожуПароль(String password){
        stepDifinitionAuth.setPasswordOnPageLogin(password);
        stepDifinitionAuth.clickButtonLoginOnPageLogin();
    }

    @Тогда("меня перенаправляет {string}")
    public void меняПеренаправляет(String url) {
        String actualUrl = stepDifinitionAuth.getCurrentUrl();

        Assert.assertEquals(actualUrl, url);
    }
}
