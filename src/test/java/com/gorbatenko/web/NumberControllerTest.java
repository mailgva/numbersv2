package com.gorbatenko.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gorbatenko.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NumberControllerTest {

    private static final String REST_URL = "/api/v1/";

    private static final String FACT = "fact";

    private static final String ERROR = "error";

    private static final String SUCCESSRATE = "successrate";

    private static final String AVGLATENCY = "avglatency";

    private static final String MOSTPOPULAR = "mostpopular";

    @Test
    void getCacheNumberFact(@Autowired MockMvc mvc) throws Exception {
        int number          = 50;

        MvcResult result1 = mvc.perform(get(String.format("%scachefact/%d", REST_URL, number)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        MvcResult result2 = mvc.perform(get(String.format("%scachefact/%d", REST_URL, number)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response1 = result1.getResponse().getContentAsString();
        String response2 = result2.getResponse().getContentAsString();

        assertEquals(response1, response2);
    }

    @Test
    void getCacheNumberFactByType(@Autowired MockMvc mvc) throws Exception {
        int number          = 2000;

        String type         = "year";

        MvcResult result1 = mvc.perform(get(String.format("%scachefact/%d/%s", REST_URL, number, type)))
                .andDo(print())
                .andReturn();

        MvcResult result2 = mvc.perform(get(String.format("%scachefact/%d/%s", REST_URL, number, type)))
                .andDo(print())
                .andReturn();

        String response1 = result1.getResponse().getContentAsString();
        String response2 = result2.getResponse().getContentAsString();

        assertEquals(response1, response2);
    }

    @Test
    void getNumberFact(@Autowired MockMvc mvc) throws Exception {
        int number          = 10;
        String partResponse = number + " is ";

        MvcResult result = mvc.perform(get(String.format("%sfact/%d", REST_URL, number)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(existsJsonKey(response, FACT));
        assertTrue(getJsonValueAsString(response, FACT).startsWith(partResponse));
    }

    @Test
    void getNumberFactByType(@Autowired MockMvc mvc) throws Exception {
        int number          = 15;
        String type         = "math";
        String partResponse = number + " is ";

        MvcResult result = mvc.perform(get(String.format("%sfact/%d/%s", REST_URL, number, type)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(existsJsonKey(response, FACT));
        assertTrue(getJsonValueAsString(response, FACT).startsWith(partResponse));
    }

    @Test
    void getNumberFactByTypeWithWrongType(@Autowired MockMvc mvc) throws Exception {
        int number          = 15;
        String type         = "abc";

        MvcResult result = mvc.perform(get(String.format("%sfact/%d/%s", REST_URL, number, type)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(existsJsonKey(response, ERROR));
    }

    @Test
    void getMostPopular(@Autowired MockMvc mvc) throws Exception {
        MvcResult result = mvc.perform(get(String.format("%s/%s/", REST_URL, MOSTPOPULAR)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(existsJsonKey(response, MOSTPOPULAR));

        assertTrue(getJsonValueAsMap(response, MOSTPOPULAR).size() <= 10);
    }

    @Test
    void getSuccessRate(@Autowired MockMvc mvc) throws Exception {
        MvcResult result = mvc.perform(get(String.format("%s/%s/", REST_URL, SUCCESSRATE)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(existsJsonKey(response, SUCCESSRATE));
    }

    @Test
    void getAvgLatency(@Autowired MockMvc mvc) throws Exception {
        MvcResult result = mvc.perform(get(String.format("%s/%s/", REST_URL, AVGLATENCY)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertTrue(existsJsonKey(response, AVGLATENCY));
    }


}