package com.example.facieaiprojecttest.service;

import com.example.facieaiprojecttest.model.EnrichedTrade;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeFileServiceTest {

    @Mock
    private TradeService tradeService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TradeFileService tradeFileService;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testProcessFileEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "", "", new byte[0]);

        tradeFileService.processFile(file, "format", "acceptHeader", response).get();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).getWriter();
    }

    @Test
    void testProcessFileFileReadError() throws Exception {
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Test error"));

        tradeFileService.processFile(file, "format", "acceptHeader", response).get();

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response).getWriter();
    }

    @Test
    void testProcessFileProcessingErrorHandledGracefully() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain",
                "content".getBytes());

        when(tradeService.processTradesAsync(any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Test error")));

        CompletableFuture<Void> future = tradeFileService.processFile(file, "format", "acceptHeader",
                response);

        future.get();

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response).getWriter();
    }

    @Test
    void testProcessFileSuccessfulProcessingJson() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain",
                "content".getBytes());
        EnrichedTrade enrichedTrade = new EnrichedTrade();
        enrichedTrade.setDate("20230101");
        enrichedTrade.setProductName("Product A");
        enrichedTrade.setCurrency("USD");
        enrichedTrade.setPrice(10.0);
        List<EnrichedTrade> enrichedTrades = List.of(enrichedTrade);
        when(tradeService.processTradesAsync(any(), any())).thenReturn(CompletableFuture
                .completedFuture(enrichedTrades));

        tradeFileService.processFile(file, "format", "application/json", response).get();

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).getWriter();
    }

    @Test
    void testProcessFileSuccessfulProcessingCsv() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain",
                "content".getBytes());
        EnrichedTrade enrichedTrade = new EnrichedTrade();
        enrichedTrade.setDate("20230101");
        enrichedTrade.setProductName("Product A");
        enrichedTrade.setCurrency("USD");
        enrichedTrade.setPrice(10.0);
        List<EnrichedTrade> enrichedTrades = List.of(enrichedTrade);
        when(tradeService.processTradesAsync(any(), any())).thenReturn(CompletableFuture
                .completedFuture(enrichedTrades));

        tradeFileService.processFile(file, "format", "text/csv", response).get();

        verify(response).setContentType("text/csv");
        verify(response).setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=enriched_trades.csv");
        verify(response).getWriter();
    }
}

