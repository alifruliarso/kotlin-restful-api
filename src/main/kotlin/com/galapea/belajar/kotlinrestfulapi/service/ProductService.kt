package com.galapea.belajar.kotlinrestfulapi.service

import com.galapea.belajar.kotlinrestfulapi.model.CreateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.ProductResponse
import com.galapea.belajar.kotlinrestfulapi.model.UpdateProductRequest

interface ProductService {
    fun create(createProductRequest: CreateProductRequest): ProductResponse
    fun get(id: String): ProductResponse
    fun update(id:String, updateProductRequest: UpdateProductRequest): ProductResponse
}