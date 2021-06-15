package com.bavostepbros.leap.integrationtest;

import com.bavostepbros.leap.integrationtest.testconfiguration.RequestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public abstract class ApiIntegrationTest {
    private String jwt;

    @Autowired
    private RequestFactory requestFactory;

    
    /** 
     * @throws Exception
     */
    protected void authenticate() throws Exception {
        jwt = requestFactory.authenticate();
    }
    
    /** 
     * @param url
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder post(String url) {
        return requestFactory.post(url, jwt);
    }
    
    /** 
     * @param url
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder put(String url) {
        return requestFactory.put(url, jwt);
    }
    
    /** 
     * @param url
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder get(String url) {
        return requestFactory.get(url, jwt);
    }
    
    /** 
     * @param url
     * @return MockHttpServletRequestBuilder
     */
    protected MockHttpServletRequestBuilder delete(String url) {
        return requestFactory.delete(url, jwt);
    }
}
