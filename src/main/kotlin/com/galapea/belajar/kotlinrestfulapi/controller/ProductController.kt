package com.galapea.belajar.kotlinrestfulapi.controller

import com.galapea.belajar.kotlinrestfulapi.model.CreateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.ProductResponse
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
    @ResponseBody
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
}