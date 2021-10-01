package com.al.agency.cucumber.selenium;

import org.springframework.stereotype.Component;

@Component
public class StepDifinitionAuth extends SeleniumDriver {
    public String getCurrentUrl(){
        String url= webDriver.getCurrentUrl().replaceAll("http://", "");
        int payloadBegin=url.indexOf("/");
        int payloadEnd=url.indexOf("?");
        return url.substring(payloadBegin, payloadEnd!=-1?payloadEnd:url.length());
    }
}
