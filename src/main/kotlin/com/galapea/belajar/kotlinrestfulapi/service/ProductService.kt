package com.galapea.belajar.kotlinrestfulapi.service

import com.galapea.belajar.kotlinrestfulapi.model.*

interface ProductService {
    fun create(createProductRequest: CreateProductRequest): ProductResponse
    fun get(id: String): ProductResponse
    fun update(id: String, updateProductRequest: UpdateProductRequest): ProductResponse
    fun delete(id: String)
    fun list(listProductRequest: ListProductRequest): PageListProduct
}