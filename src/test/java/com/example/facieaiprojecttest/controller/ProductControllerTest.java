package com.example.facieaiprojecttest.controller;

import com.example.facieaiprojecttest.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getProductNameShouldReturnProductName() throws Exception {
        String productId = "123";
        String expectedName = "Test Product";

        when(productService.getProductName(productId)).thenReturn(expectedName);

        mockMvc.perform(get("/products/{productId}", productId))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).getProductName(productId);
    }

    @Test
    void getProductNameShouldReturnEmptyWhenProductIsMissing() throws Exception {
        String productId = "999";

        when(productService.getProductName(productId)).thenReturn(null);

        mockMvc.perform(get("/products/{productId}", productId))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).getProductName(productId);
    }

    @Test
    void getProductNameShouldReturnMultiLineProductName() throws Exception {
        String productId = "123";
        String expectedName = "Line 1\nLine 2";

        when(productService.getProductName(productId)).thenReturn(expectedName);

        mockMvc.perform(get("/products/{productId}", productId))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).getProductName(productId);
    }
}



