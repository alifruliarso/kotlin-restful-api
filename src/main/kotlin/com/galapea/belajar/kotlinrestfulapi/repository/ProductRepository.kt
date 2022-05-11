package com.galapea.belajar.kotlinrestfulapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.galapea.belajar.kotlinrestfulapi.entity.Product

interface ProductRepository: JpaRepository<Product, String>{

}