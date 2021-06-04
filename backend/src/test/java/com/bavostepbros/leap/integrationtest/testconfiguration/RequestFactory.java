package com.bavostepbros.leap.integrationtest.testconfiguration;

import org.mockito.Mock;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class RequestFactory {
    public static MockHttpServletRequestBuilder buildRequest(String url, String jwt) {
        return MockMvcRequestBuilders.get(url)
                .header("Authorization", "Bearer " + jwt);
    }
}
