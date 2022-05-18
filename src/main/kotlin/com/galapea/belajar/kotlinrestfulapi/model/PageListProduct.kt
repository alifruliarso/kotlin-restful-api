package com.galapea.belajar.kotlinrestfulapi.model

import java.util.*

data class PageListProduct (
    val listProduct: List<ProductResponse>?,
    val currentPage: Int,
    val totalItems: Int,
    val totalPages: Int,
    val first: Boolean
)