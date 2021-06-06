package com.bavostepbros.leap.integrationtest.testconfiguration;

import org.mockito.Mock;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
public class RequestFactory {
    public MockHttpServletRequestBuilder buildRequest(String url, String jwt) {
        return MockMvcRequestBuilders.get(url)
                .header("Authorization", "Bearer " + jwt);
    }
}
