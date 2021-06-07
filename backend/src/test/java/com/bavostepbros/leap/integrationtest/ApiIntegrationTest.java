package com.bavostepbros.leap.integrationtest;

import com.bavostepbros.leap.integrationtest.testconfiguration.RequestFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public abstract class ApiIntegrationTest {
    private String jwt;

    @Autowired
    protected RequestFactory requestFactory;

    protected void authenticate() throws Exception {
        jwt = requestFactory.authenticate();
    }

    protected MockHttpServletRequestBuilder post(String url) {
        return requestFactory.post(url, jwt);
    }


    protected MockHttpServletRequestBuilder put(String url) {
        return requestFactory.put(url, jwt);
    }

    protected MockHttpServletRequestBuilder get(String url) {
        return requestFactory.get(url, jwt);
    }

    protected MockHttpServletRequestBuilder delete(String url) {
        return requestFactory.delete(url, jwt);
    }
}
