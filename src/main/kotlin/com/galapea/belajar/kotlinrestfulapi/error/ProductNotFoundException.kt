package com.galapea.belajar.kotlinrestfulapi.error

class ProductNotFoundException(id: String) : RuntimeException("Product:$id is not found") {
}