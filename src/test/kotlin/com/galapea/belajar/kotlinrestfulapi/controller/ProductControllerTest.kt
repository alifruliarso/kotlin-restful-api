package com.galapea.belajar.kotlinrestfulapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.galapea.belajar.kotlinrestfulapi.model.CreateProductRequest
import com.galapea.belajar.kotlinrestfulapi.model.ProductResponse
import com.galapea.belajar.kotlinrestfulapi.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*


@WebMvcTest(controllers = [ProductController::class])
class ProductControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @MockBean
    lateinit var productService: ProductService

    @Test
    fun whenPostCreateProduct_thenReturnNewProduct() {
        val createProductRequest = CreateProductRequest(id = "x123", name = "A gift", price = 6990, quantity = 3)
        val productResponse = ProductResponse(
            id = createProductRequest.id,
            name = createProductRequest.name,
            price = createProductRequest.price,
            quantity = createProductRequest.quantity,
            createdAt = Date(),
            updatedAt = null
        )
        whenever(productService.create(createProductRequest)).thenReturn(productResponse)
        mockMvc.perform(
            post("/api/products").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductRequest))
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.id").value("x123"))
            .andExpect(jsonPath("$.data.name").value("A gift"))
    }
}