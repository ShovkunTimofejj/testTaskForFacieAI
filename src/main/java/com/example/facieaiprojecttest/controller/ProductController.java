package com.example.facieaiprojecttest.controller;

import com.example.facieaiprojecttest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Product Controller", description = "API for managing products")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get product name",
            description = "Retrieves the name of a product by its ID. The name is returned as plain text.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product name retrieved successfully",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string", description = "Product name"))),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string", description = "Empty string"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping(path = "{productId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public Stream<String> getProductName(@PathVariable String productId) {
        String name = productService.getProductName(productId);
        if (name == null) {
            return Stream.of("");
        }
        return name.lines();
    }
}



