package com.example.facieaiprojecttest.controller;

import com.example.facieaiprojecttest.service.TradeFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Trade Controller", description = "API for processing trade files and enriching trade data.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TradeController {

    private final TradeFileService tradeFileService;

    @Operation(
            summary = "Enrich trades from uploaded file",
            description = "Processes an uploaded trade file and enriches the trade data. The file is uploaded as `multipart/form-data` and processed in either CSV, JSON, or XML format.",
            parameters = {
                    @Parameter(name = "file", description = "The trade file to enrich", required = true, content = @Content(mediaType = "multipart/form-data")),
                    @Parameter(name = "format", description = "The format to process the file, default is 'csv'", required = false, schema = @Schema(defaultValue = "csv")),
                    @Parameter(name = HttpHeaders.ACCEPT, description = "The format of the response (e.g., 'application/json', 'text/csv', or 'application/xml')", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "File processed successfully and enriched",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = "string", description = "The enriched data in JSON, CSV, or XML format"))),
                    @ApiResponse(responseCode = "200", description = "File processed successfully and enriched in XML format",
                            content = @Content(mediaType = "application/xml",
                                    schema = @Schema(type = "string", description = "The enriched data in XML format"))),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid file or format",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Error message describing the issue"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Error message describing the issue")))
            }
    )
    @PostMapping(value = "/enrich", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE, "text/csv", MediaType.APPLICATION_XML_VALUE})
    public CompletableFuture<Void> enrichTrades(
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "csv") String format,
            @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader,
            HttpServletResponse response) {

        return tradeFileService.processFile(file, format, acceptHeader, response);
    }
}


