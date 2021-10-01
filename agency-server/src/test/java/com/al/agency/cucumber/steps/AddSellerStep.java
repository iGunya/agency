package com.al.agency.cucumber.steps;

import com.al.agency.cucumber.selenium.StepDifinitionAddSeller;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class AddSellerStep {
    @Autowired
    StepDifinitionAddSeller stepAddSeller;


    @Допустим("менеджер авторизовался c логином {string} и паролем {string} на странице {string}")
    public void менеджерАвторизовалсяCЛогиномИПаролемНаСтранице(
            String login,
            String password,
            String url
    ) throws InterruptedException {
        stepAddSeller.getPageByURL(url);
        stepAddSeller.setLoginOnPageLogin(login);
        stepAddSeller.setPasswordOnPageLogin(password);
        stepAddSeller.clickButtonLoginOnPageLogin();
    }

    @Когда("менеджер перейдет на {string}")
    public void менеджерПерейдетНа(String url) throws InterruptedException {
        stepAddSeller.getPageByURL(url);
    }

    @Тогда("открвыется страница добавления продавцов")
    public void открвыетсяСтраницаДобавленияПродавцов() {

    }

    @И("менеджер вводит данные продавца")
    public void менеджерВводитДанныеПродавца() {
        stepAddSeller.inputParamSeller();
    }

    @И("нажимает кнопку сохранить продавца")
    public void нажимаетКнопкуСохранитьПродавца() {
        stepAddSeller.clickSaveButton();
    }

    @Тогда("при успешном сохранении меня перенаправляет на {string}")
    public void приУспешномСохраненииМеняПеренаправляетНа(String url) {
        String actualUrl = stepAddSeller.getCurrentUrl();

        Assert.assertEquals(actualUrl, url);
    }



}
