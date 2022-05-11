package com.galapea.belajar.kotlinrestfulapi.service

import com.galapea.belajar.kotlinrestfulapi.model.CreateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.ProductResponse

interface ProductService {
    fun create(createProductRequest: CreateProductRequest): ProductResponse
}