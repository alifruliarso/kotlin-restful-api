package com.galapea.belajar.kotlinrestfulapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.galapea.belajar.kotlinrestfulapi.error.ProductNotFoundException
import com.galapea.belajar.kotlinrestfulapi.model.*
import com.galapea.belajar.kotlinrestfulapi.service.ProductService
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*
import javax.validation.ConstraintViolationException
import javax.validation.Validation


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

    @Test
    fun givenInvalidRequest_whenPostCreateProduct_thenReturnStatus400() {
        val createProductRequest = CreateProductRequest(id = "x123", name = "A gift", price = 0, quantity = -1)
        val validatorFactory = Validation.buildDefaultValidatorFactory()
        val validator = validatorFactory.validator
        val constraints = validator.validate(createProductRequest)
        whenever(productService.create(createProductRequest)).then { throw ConstraintViolationException(constraints) }

        val mvcResult = mockMvc.perform(
            post("/api/products").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductRequest))
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", `is`(400)))
            .andExpect(jsonPath("$.status", `is`("Bad Request")))
            .andExpect(jsonPath("$.data").isNotEmpty)
            .andExpect(jsonPath("$.data.code", containsString("validation_failure")))
            .andExpect(jsonPath("$.data.message", containsString("Validation failed.")))
            .andExpect(jsonPath("$.data.errors[*].path", containsInAnyOrder("price", "quantity")))
            .andExpect(
                jsonPath(
                    "$.data.errors[*].message", containsInAnyOrder(
                        "must be greater than or equal to 1",
                        "must be greater than or equal to 0"
                    )
                )
            )
            .andReturn()
        println(mvcResult.response.contentAsString)
    }

    @Test
    fun whenGetProductById_thenReturnProduct() {
        val id = "s11"
        val name = "name product"
        val productResponse = ProductResponse(
            id = id,
            name = name,
            price = 123,
            quantity = 12,
            createdAt = Date(),
            updatedAt = null
        )

        whenever(productService.get(id = id)).thenReturn(productResponse)
        mockMvc.perform(
            get("/api/products/$id")
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.id").value(id))
            .andExpect(jsonPath("$.data.name").value(name))
    }

    @Test
    fun whenGetProductByIdNotFound_thenReturnError() {
        val id = "s11"
        whenever(productService.get(id = id)).then { throw ProductNotFoundException(id) }
        mockMvc.perform(
            get("/api/products/$id")
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", `is`(404)))
            .andExpect(jsonPath("$.status", `is`("Not Found")))
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    fun whenPutUpdateProduct_thenReturnProduct() {
        val productId = "3"
        val productName = "A name"
        val productPrice = 3456L
        val productQuantity = 4
        val updateProductRequest =
            UpdateProductRequest(name = productName, price = productPrice, quantity = productQuantity)
        val productResponse = ProductResponse(
            id = productId,
            name = productName,
            price = productPrice,
            quantity = productQuantity,
            createdAt = Date(),
            updatedAt = null
        )
        whenever(productService.update(id = productId, updateProductRequest)).thenReturn(productResponse)

        mockMvc.perform(
            put("/api/products/$productId").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProductRequest))
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.id").value(productId))
            .andExpect(jsonPath("$.data.name").value(productName))
            .andExpect(jsonPath("$.data.price").value(productPrice))
            .andExpect(jsonPath("$.data.quantity").value(productQuantity))
    }

    @Test
    fun givenInvalidRequest_whenUpdateProduct_thenReturnStatus400() {
        val productId = "3"
        val updateProductRequest = UpdateProductRequest(name = "Updated name", price = 0, quantity = -3)
        val validatorFactory = Validation.buildDefaultValidatorFactory()
        val validator = validatorFactory.validator
        val constraints = validator.validate(updateProductRequest)
        whenever(productService.update(id = productId, updateProductRequest)).then {
            throw ConstraintViolationException(
                constraints
            )
        }

        val mvcResult = mockMvc.perform(
            put("/api/products/$productId").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProductRequest))
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", `is`(400)))
            .andExpect(jsonPath("$.status", `is`("Bad Request")))
            .andExpect(jsonPath("$.data").isNotEmpty)
            .andExpect(jsonPath("$.data.code", containsString("validation_failure")))
            .andExpect(jsonPath("$.data.message", containsString("Validation failed.")))
            .andExpect(jsonPath("$.data.errors[*].path", containsInAnyOrder("price", "quantity")))
            .andExpect(
                jsonPath(
                    "$.data.errors[*].message", containsInAnyOrder(
                        "must be greater than or equal to 1",
                        "must be greater than or equal to 0"
                    )
                )
            )
            .andReturn()
        println(mvcResult.response.contentAsString)
    }

    @Test
    fun whenDeleteProduct_thenReturn200() {
        mockMvc.perform(
            delete("/api/products/12345")
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", `is`(200)))
            .andExpect(jsonPath("$.status", `is`("Deleted")))
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    fun givenInvalidId_whenDeleteProduct_thenReturnStatus404() {
        whenever(productService.delete(id = "id")).then { throw ProductNotFoundException("id") }

        mockMvc.perform(
            delete("/api/products/id")
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", `is`(404)))
            .andExpect(jsonPath("$.status", `is`("Not Found")))
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    fun whenGetProductList_thenReturnListOfProduct() {
        val listProductResponse = generateListProductResponse()
        val listProductRequest = ListProductRequest(page = 1, size = 3)
        val pageListProduct = PageListProduct(
            totalItems = 5,
            totalPages = 2,
            currentPage = 1,
            listProduct = listProductResponse,
            first = true
        )
        whenever(productService.list(listProductRequest)).thenReturn(pageListProduct)

        mockMvc.perform(
            get("/api/products").param("size", "3").param("page", "1")
        )
            .andDo { MockMvcResultHandlers.print() }
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").isNotEmpty)
            .andExpect(jsonPath("$.data.totalItems", `is`(5)))
            .andExpect(jsonPath("$.data.totalPages", `is`(2)))
            .andExpect(jsonPath("$.data.currentPage", `is`(1)))
            .andExpect(jsonPath("$.data.first", `is`(true)))
            .andExpect(jsonPath("$.data.listProduct.length()", `is`(5)))
            .andExpect(jsonPath("$.data.listProduct[*].id", containsInAnyOrder("1", "2", "3", "5", "4")))
            .andExpect(
                jsonPath(
                    "$.data.listProduct[*].name",
                    containsInAnyOrder("name1", "name2", "name3", "name4", "name5")
                )
            )
    }

    private fun generateListProductResponse(): List<ProductResponse> {
        return listOf(
            ProductResponse(
                id = "1",
                name = "name1",
                price = 123,
                quantity = 12,
                createdAt = Date(),
                updatedAt = null
            ),
            ProductResponse(
                id = "2",
                name = "name2",
                price = 99,
                quantity = 33,
                createdAt = Date(),
                updatedAt = null
            ),
            ProductResponse(
                id = "3",
                name = "name3",
                price = 99,
                quantity = 33,
                createdAt = Date(),
                updatedAt = null
            ),
            ProductResponse(
                id = "4",
                name = "name4",
                price = 99,
                quantity = 33,
                createdAt = Date(),
                updatedAt = null
            ),
            ProductResponse(
                id = "5",
                name = "name5",
                price = 99,
                quantity = 33,
                createdAt = Date(),
                updatedAt = null
            )
        )
    }
}