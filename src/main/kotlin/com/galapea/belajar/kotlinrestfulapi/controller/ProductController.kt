package com.galapea.belajar.kotlinrestfulapi.controller

import com.galapea.belajar.kotlinrestfulapi.model.CreateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.ProductResponse
import com.galapea.belajar.kotlinrestfulapi.model.UpdateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.WebResponse
import com.galapea.belajar.kotlinrestfulapi.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
class ProductController(val productService: ProductService) {

    @PostMapping(
        value = ["/api/products"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun create(@RequestBody createProductRequest: CreateProductRequest): WebResponse<ProductResponse> {
        val productResponse = productService.create(createProductRequest)
        return WebResponse(
            code = 200,
            status = "Created",
            data = productResponse
        )
    }

    @GetMapping(
        value = ["/api/products/{productId}"]
    )
    fun get(@PathVariable(name = "productId") productId: String): WebResponse<ProductResponse> {
        val productResponse = productService.get(productId)
        return WebResponse(
            code = 200,
            status = "OK",
            data = productResponse
        )
    }

    @PutMapping(
        value = ["/api/products/{productId}"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun update(
        @PathVariable(name = "productId") productId: String,
        @RequestBody updateProductRequest: UpdateProductRequest
    ): WebResponse<ProductResponse> {
        val productResponse = productService.update(id = productId, updateProductRequest)
        return WebResponse(
            code = 200,
            status = "Updated",
            data = productResponse
        )
    }
}