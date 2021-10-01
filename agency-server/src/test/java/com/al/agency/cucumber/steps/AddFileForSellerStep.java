package com.al.agency.cucumber.steps;

import com.al.agency.cucumber.selenium.StepDifinitionAddFileForSeller;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class AddFileForSellerStep {
    @Autowired
    StepDifinitionAddFileForSeller stepAddFile;

    private int countBeforeAdd;

    @Допустим("пользвователь авторизировался как менеджер c логином {string} и паролем {string} на странице {string}")
    public void пользвовательАвторизировалсяКакМенеджерCЛогиномИПаролемНаСтранице(
            String login,
            String password,
            String url
    ) throws InterruptedException {
        Thread.sleep(1000);
        stepAddFile.getPageByURL(url);
        stepAddFile.setLoginOnPageLogin(login);
        stepAddFile.setPasswordOnPageLogin(password);
        stepAddFile.clickButtonLoginOnPageLogin();
    }

    @Тогда("открвыется страница продавцов")
    public void открвыетсяСтраницаПродавцов() {
        countBeforeAdd = stepAddFile.countFileBeforeAdd();
    }

    @И("менеджер выбирает файл для добавления к первому в списке продавцу")
    public void менеджерВыбираетФайлДляДобавленияКПервомуВСпискеПродавцу() {
        stepAddFile.choseFile("PATH");
    }

    @И("нажимает кнопку сохранить файл")
    public void нажимаетКнопкуСохранитьФайл() {
        stepAddFile.clickSaveFile();
    }

    @Тогда("у продавца появляется в списке файлов новый файл")
    public void уПродавцаПоявляетсяВСпискеФайловНовыйФайл() {
        int countAfterAdd = stepAddFile.countFileAfterAdd();

        Assert.assertEquals(1, countAfterAdd-countBeforeAdd);
    }

}
