package com.yhproject.mywiki

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiGeneratorTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Test
    fun generateOpenApiJson() {
        val result = mockMvc.perform(get("/v3/api-docs")).andReturn()

        val json = result.response.contentAsString
        assertNotNull(json)
        Files.writeString(Paths.get("openapi.json"), json)
    }
}
