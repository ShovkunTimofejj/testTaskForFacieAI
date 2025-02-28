package com.example.facieaiprojecttest.controller;

import com.example.facieaiprojecttest.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Redis Controller", description = "API for interacting with Redis")
@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @Operation(
            summary = "Save data to Redis",
            description = "Stores a key-value pair in Redis with an expiration time of 10 seconds.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Data saved successfully",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Confirmation message"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/save")
    public String saveData(
            @Parameter(description = "The key to store data under") @RequestParam String key,
            @Parameter(description = "The value to store for the key") @RequestParam String value) {
        redisService.saveData(key, value, 10);
        return "Data saved!";
    }

    @Operation(
            summary = "Get data from Redis",
            description = "Retrieves the value associated with the provided key from Redis.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Data retrieved successfully",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Value associated with the key"))),
                    @ApiResponse(responseCode = "404", description = "Key not found in Redis",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Empty string or error message"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/get")
    public String getData(
            @Parameter(description = "The key to retrieve the associated value") @RequestParam String key) {
        return redisService.getData(key);
    }

    @Operation(
            summary = "Delete data from Redis",
            description = "Deletes the key-value pair associated with the provided key from Redis.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Data deleted successfully",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Confirmation message"))),
                    @ApiResponse(responseCode = "404", description = "Key not found in Redis",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "string", description = "Error message"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/delete")
    public String deleteData(
            @Parameter(description = "The key to delete the associated value") @RequestParam String key) {
        redisService.deleteData(key);
        return "Data deleted!";
    }
}
