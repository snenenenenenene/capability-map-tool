package com.bavostepbros.leap.integrationtest.testconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
public class RequestFactory {

    @Autowired
    private MockMvc mockMvc;

    
    /** 
     * @param url
     * @param jwt
     * @return MockHttpServletRequestBuilder
     */
    public MockHttpServletRequestBuilder get(String url, String jwt) {
        return MockMvcRequestBuilders.get(url)
                .header("Authorization", "Bearer " + jwt);
    }

    
    /** 
     * @param url
     * @param jwt
     * @return MockHttpServletRequestBuilder
     */
    public MockHttpServletRequestBuilder post(String url, String jwt) {
        return MockMvcRequestBuilders.post(url)
                .header("Authorization", "Bearer " + jwt);
    }

    
    /** 
     * @param url
     * @param jwt
     * @return MockHttpServletRequestBuilder
     */
    public MockHttpServletRequestBuilder put(String url, String jwt) {
        return MockMvcRequestBuilders.put(url)
                .header("Authorization", "Bearer " + jwt);
    }

    
    /** 
     * @param url
     * @param jwt
     * @return MockHttpServletRequestBuilder
     */
    public MockHttpServletRequestBuilder delete(String url, String jwt) {
        return MockMvcRequestBuilders.delete(url)
                .header("Authorization", "Bearer " + jwt);
    }

    
    /** 
     * @return String
     * @throws Exception
     */
    public String authenticate() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/user/authenticate")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("email", "super_admin")
                .param("password", "super_admin"))
                .andReturn()
                .getResponse().getContentAsString();
    }
}
