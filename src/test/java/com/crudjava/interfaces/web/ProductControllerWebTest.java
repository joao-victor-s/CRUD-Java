package com.crudjava.interfaces.web;

import com.crudjava.interfaces.ProductController;
import com.crudjava.application.dto.ProductRequest;
import com.crudjava.application.dto.ProductResponse;
import com.crudjava.application.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Test;


@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
public class ProductControllerWebTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean ProductService productService;

    @Test
    void shouldReturn200WhenFindById()  throws Exception{
        ProductResponse resp = new ProductResponse(10L, "Teclado", 200.99);

        Mockito.when (productService.getProductById(10L)).thenReturn(resp);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}",10L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(10L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Teclado"));

        Mockito.verify(productService).getProductById(10L);
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    void shouldReturn201AndLocationWhenCreate()  throws Exception{
        ProductRequest req = new ProductRequest("Mouse", 200.99);
        ProductResponse resp = new ProductResponse(20L, "Mouse", 200.99);

        // Quando o saveProduct do ProductService for chamado com qualquer ProductRequest, retorne o ProductResponse resp (mock)
        Mockito.when (productService.saveProduct(ArgumentMatchers.any(ProductRequest.class))).thenReturn(resp);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/products/20"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(20L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mouse"));

        Mockito.verify(productService).saveProduct(ArgumentMatchers.any(ProductRequest.class));
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    void shouldReturn200WhenUpdate() throws Exception {
        ProductRequest req = new ProductRequest("Monitor", 899.00);
        ProductResponse resp = new ProductResponse(7L, "Monitor", 899.00);

        Mockito.when(productService.updateProduct(ArgumentMatchers.any(ProductRequest.class), ArgumentMatchers.eq(7L)))
                .thenReturn(resp);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Monitor"));

        Mockito.verify(productService).updateProduct(ArgumentMatchers.any(ProductRequest.class), ArgumentMatchers.eq(7L));
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    void shouldReturn204WhenDelete() throws Exception {
        Mockito.doNothing().when(productService).deleteProductById(9L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", 9L))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));

        Mockito.verify(productService).deleteProductById(9L);
        Mockito.verifyNoMoreInteractions(productService);
    }
}
