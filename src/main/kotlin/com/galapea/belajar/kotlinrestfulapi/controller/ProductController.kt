package com.galapea.belajar.kotlinrestfulapi.controller

import com.galapea.belajar.kotlinrestfulapi.model.*
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

    @DeleteMapping(
        value = ["/api/products/{productId}"],
        produces = ["application/json"]
    )
    fun delete(@PathVariable(name = "productId") productId: String): WebResponse<ProductResponse> {
        productService.delete(id = productId)
        return WebResponse(
            code = 200,
            status = "Deleted",
            data = null
        )
    }

    @GetMapping(
        value = ["/api/products"],
        produces = ["application/json"]
    )
    fun get(
        @RequestParam(name = "size", defaultValue = "10") size: Int,
        @RequestParam(name = "page", defaultValue = "1") page: Int
    ): WebResponse<PageListProduct> {
        val listProductRequest = ListProductRequest(page = page, size = size)
        val productResponse = productService.list(listProductRequest)
        return WebResponse(
            code = 200,
            status = "OK",
            data = productResponse
        )
    }
}