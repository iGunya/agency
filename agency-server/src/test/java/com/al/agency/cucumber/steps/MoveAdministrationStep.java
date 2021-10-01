package com.al.agency.cucumber.steps;

import com.al.agency.cucumber.selenium.StepDifinitionMoveAdmin;
import io.cucumber.java.ru.*;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class MoveAdministrationStep {
    @Autowired
    StepDifinitionMoveAdmin stepMoveAdmin;

    @Допустим("пользвователь авторизировался как администратор c логином {string} и паролем {string} на странице {string}")
    public void пользвовательАвторизировалсяКакАдминистраторCЛогиномИПаролемНаСтранице(
            String login,
            String password,
            String url
    ) throws InterruptedException {
        stepMoveAdmin.getPageByURL(url);
        stepMoveAdmin.setLoginOnPageLogin(login);
        stepMoveAdmin.setPasswordOnPageLogin(password);
        stepMoveAdmin.clickButtonLoginOnPageLogin();
    }

    @Пусть("на странице {string}")
    public void наСтранице(String url) throws InterruptedException {
        stepMoveAdmin.getPageByURL(url);
    }

    @Когда("админ вводит новый {string} пользователю")
    public void админВводитНовыйПользователю(String login) {
        stepMoveAdmin.setNewLoginUser(login);
    }

    @Когда("админ вводит существующий у другого пользователя {string}")
    public void админВводитСуществующийУДругогоПользователя(String login) {
        stepMoveAdmin.setNotUnicueLoginUser(login);
    }

    @И("нажимает кнопку сохранить")
    public void нажимаетКнопкуСохранить() {
        stepMoveAdmin.clickSaveButton();
    }

    @Тогда("на месте кнопки подтвердить появляется надпись {string}")
    public void наМестеКнопкиПодтвердитьПоявляетсяНадпись(String label) throws InterruptedException {
        String actualLabel = stepMoveAdmin.checkStatusUpdate();

        Assert.assertEquals(actualLabel, label);
    }


}
