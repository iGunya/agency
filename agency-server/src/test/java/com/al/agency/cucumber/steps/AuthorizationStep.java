package com.al.agency.cucumber.steps;

import com.al.agency.cucumber.selenium.StepDefinitionAuthorization;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Но;
import io.cucumber.java.ru.Тогда;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorizationStep {
    @Autowired
    private StepDefinitionAuthorization stepAuthorization;

    @Когда("я прохожу по ссылке {string}")
    public void яПрохожуПоСсылке(String url) throws InterruptedException {
        stepAuthorization.getPageByURL(url);
    }

    @Тогда("я должен ввести {string}")
    public void яДолженВвести(String login) {
        stepAuthorization.setLoginOnPageLogin(login);
    }

    @И("я пароль {string}")
    public void яПароль(String password) {
        stepAuthorization.setPasswordOnPageLogin(password);
        stepAuthorization.clickButtonLoginOnPageLogin();
    }

    @Но("оказывается у меня нет прав доступа <{string}>")
    public void оказываетсяУМеняНетПравДоступа(String status) throws InterruptedException {
        Assert.assertTrue(stepAuthorization.checkStatus403());
    }
}
