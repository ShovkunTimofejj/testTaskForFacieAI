package com.example.facieaiprojecttest.controller;

import com.example.facieaiprojecttest.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RedisControllerTest {

    @Mock
    private RedisService redisService;

    @InjectMocks
    private RedisController redisController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(redisController).build();
    }

    @Test
    void saveDataShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post("/redis/save")
                        .param("key", "testKey")
                        .param("value", "testValue"))
                .andExpect(status().isOk())
                .andExpect(content().string("Data saved!"));

        verify(redisService, times(1)).saveData("testKey", "testValue", 10);
    }

    @Test
    void getDataShouldReturnStoredValue() throws Exception {
        when(redisService.getData("testKey")).thenReturn("testValue");

        mockMvc.perform(get("/redis/get")
                        .param("key", "testKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("testValue"));

        verify(redisService, times(1)).getData("testKey");
    }

    @Test
    void deleteDataShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/redis/delete")
                        .param("key", "testKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("Data deleted!"));

        verify(redisService, times(1)).deleteData("testKey");
    }
}

