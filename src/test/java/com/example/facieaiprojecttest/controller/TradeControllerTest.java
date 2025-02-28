package com.example.facieaiprojecttest.controller;

import com.example.facieaiprojecttest.service.TradeFileService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeControllerTest {

    @Mock
    private TradeFileService tradeFileService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TradeController tradeController;

    @Captor
    private ArgumentCaptor<MultipartFile> fileCaptor;

    @Captor
    private ArgumentCaptor<String> formatCaptor;

    @Captor
    private ArgumentCaptor<String> acceptHeaderCaptor;

    @Captor
    private ArgumentCaptor<HttpServletResponse> responseCaptor;

    @Test
    void testEnrichTrades() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                "test".getBytes());
        String format = "csv";
        String acceptHeader = MediaType.APPLICATION_JSON_VALUE;

        when(tradeFileService.processFile(any(), any(), any(), any())).thenReturn(CompletableFuture
                .completedFuture(null));

        CompletableFuture<Void> result = tradeController.enrichTrades(file, format, acceptHeader, response);

        verify(tradeFileService).processFile(fileCaptor.capture(), formatCaptor.capture(), acceptHeaderCaptor.capture(),
                responseCaptor.capture());

        assertEquals(file, fileCaptor.getValue());
        assertEquals(format, formatCaptor.getValue());
        assertEquals(acceptHeader, acceptHeaderCaptor.getValue());
        assertEquals(response, responseCaptor.getValue());
    }

    @Test
    void testEnrichTradesDefaultFormat() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                "test".getBytes());
        String acceptHeader = MediaType.APPLICATION_JSON_VALUE;

        when(tradeFileService.processFile(any(), any(), any(), any())).thenReturn(CompletableFuture
                .completedFuture(null));

        CompletableFuture<Void> result = tradeController.enrichTrades(file, "csv", acceptHeader, response);

        verify(tradeFileService).processFile(fileCaptor.capture(), formatCaptor.capture(), acceptHeaderCaptor.capture(),
                responseCaptor.capture());

        assertEquals(file, fileCaptor.getValue());
        assertEquals("csv", formatCaptor.getValue());
        assertEquals(acceptHeader, acceptHeaderCaptor.getValue());
        assertEquals(response, responseCaptor.getValue());
    }

    @Test
    void testEnrichTradesMissingFormat() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                "test".getBytes());
        String acceptHeader = MediaType.APPLICATION_JSON_VALUE;

        when(tradeFileService.processFile(any(), any(), any(), any())).thenReturn(CompletableFuture
                .completedFuture(null));

        CompletableFuture<Void> result = tradeController.enrichTrades(file, "csv", acceptHeader, response);

        verify(tradeFileService).processFile(fileCaptor.capture(), formatCaptor.capture(), acceptHeaderCaptor.capture(),
                responseCaptor.capture());

        assertEquals(file, fileCaptor.getValue());
        assertEquals("csv", formatCaptor.getValue());
        assertEquals(acceptHeader, acceptHeaderCaptor.getValue());
        assertEquals(response, responseCaptor.getValue());
    }
}
