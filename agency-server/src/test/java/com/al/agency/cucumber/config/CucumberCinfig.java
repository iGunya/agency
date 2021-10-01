package com.al.agency.cucumber.config;

import com.al.agency.AgencyApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = AgencyApplication.class)
public class CucumberCinfig {
}
