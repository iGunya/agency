package com.al.agency.cucumber.steps;

import com.al.agency.cucumber.selenium.SeleniumDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {
    @Autowired
    SeleniumDriver seleniumDriver;

    @After
    public void exitAccount(){
        seleniumDriver.clearCookie();
    }
}
