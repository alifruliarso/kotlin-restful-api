package com.galapea.belajar.kotlinrestfulapi

import com.galapea.belajar.kotlinrestfulapi.controller.ProductController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KotlinRestfulApiApplicationTests(
	@Autowired val productController: ProductController
) {

	@Test
	fun contextLoads() {
		Assertions.assertThat(productController).isNotNull
	}

}
