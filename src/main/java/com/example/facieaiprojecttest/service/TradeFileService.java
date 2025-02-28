package com.example.facieaiprojecttest.service;

import com.example.facieaiprojecttest.model.EnrichedTrade;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TradeFileService {

    private static final Logger logger = LoggerFactory.getLogger(TradeFileService.class);
    private final TradeService tradeService;

    public CompletableFuture<Void> processFile(MultipartFile file, String format, String acceptHeader, HttpServletResponse response) {
        if (file.isEmpty()) {
            return handleEmptyFile(response);
        }

        try {
            String fileContent = readFileContent(file);
            return tradeService.processTradesAsync(fileContent, format)
                    .thenAccept(enrichedTrades -> writeResponseBasedOnFormat(response, enrichedTrades, acceptHeader))
                    .exceptionally(ex -> handleProcessingError(response, ex));
        } catch (IOException e) {
            return handleFileReadError(response, e);
        }
    }

    private CompletableFuture<Void> handleEmptyFile(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeResponse(response, "File is empty");
        return CompletableFuture.completedFuture(null);
    }

    private String readFileContent(MultipartFile file) throws IOException {
        return new String(file.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private CompletableFuture<Void> handleFileReadError(HttpServletResponse response, IOException e) {
        logger.error("Error reading file", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeResponse(response, "Error reading file: " + e.getMessage());
        return CompletableFuture.completedFuture(null);
    }

    private Void handleProcessingError(HttpServletResponse response, Throwable ex) {
        logger.error("Error processing file asynchronously", ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeResponse(response, "Error processing file: " + ex.getMessage());
        return null;
    }

    private void writeResponseBasedOnFormat(HttpServletResponse response, List<EnrichedTrade> enrichedTrades, String acceptHeader) {
        try {
            if (acceptHeader.contains("text/csv")) {
                response.setContentType("text/csv");
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=enriched_trades.csv");
                writeCsvToResponse(response, enrichedTrades);
            } else {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                writeResponse(response, new ObjectMapper().writeValueAsString(enrichedTrades));
            }
        } catch (IOException e) {
            logger.error("Error writing response", e);
        }
    }

    private void writeResponse(HttpServletResponse response, String message) {
        try {
            response.getWriter().write(message);
        } catch (IOException e) {
            logger.error("Error writing error response", e);
        }
    }

    private void writeCsvToResponse(HttpServletResponse response, List<EnrichedTrade> trades) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("date,productName,currency,price");
        for (EnrichedTrade trade : trades) {
            writer.println(String.join(",",
                    trade.getDate(),
                    trade.getProductName(),
                    trade.getCurrency(),
                    String.valueOf(trade.getPrice())));
        }
        writer.flush();
    }
}
