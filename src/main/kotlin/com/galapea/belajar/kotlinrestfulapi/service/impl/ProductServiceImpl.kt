package com.galapea.belajar.kotlinrestfulapi.service.impl

import com.galapea.belajar.kotlinrestfulapi.entity.Product
import com.galapea.belajar.kotlinrestfulapi.error.ProductNotFoundException
import com.galapea.belajar.kotlinrestfulapi.model.CreateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.ProductResponse
import com.galapea.belajar.kotlinrestfulapi.model.UpdateProductRequest
import com.galapea.belajar.kotlinrestfulapi.repository.ProductRepository
import com.galapea.belajar.kotlinrestfulapi.service.ProductService
import com.galapea.belajar.kotlinrestfulapi.validation.ValidationUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository,
    val validationUtil: ValidationUtil
) : ProductService {
    override fun create(createProductRequest: CreateProductRequest): ProductResponse {
        validationUtil.validate(createProductRequest)
        val product = Product(
            id = createProductRequest.id,
            name = createProductRequest.name,
            price = createProductRequest.price,
            quantity = createProductRequest.quantity,
            createdAt = Date(),
            updatedAt = null
        )
        productRepository.save(product)
        return convertProductToProductResponse(product)
    }

    override fun get(id: String): ProductResponse {
        val product = productRepository.findByIdOrNull(id = id)
        if (product == null) {
            throw ProductNotFoundException(id = id)
        } else {
            return convertProductToProductResponse(product)
        }
    }

    override fun update(id: String, updateProductRequest: UpdateProductRequest): ProductResponse {
        validationUtil.validate(updateProductRequest)
        val product = productRepository.findByIdOrNull(id = id) ?: throw ProductNotFoundException(id = id)
        product.apply {
            name = updateProductRequest.name!!
            price = updateProductRequest.price!!
            quantity = updateProductRequest.quantity!!
            updatedAt = Date()
        }
        productRepository.save(product)
        return convertProductToProductResponse(product)
    }

    private fun convertProductToProductResponse(product: Product): ProductResponse {
        return ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            quantity = product.quantity,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}