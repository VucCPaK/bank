package com.example.back;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void mustCreateNewCard() throws Exception {
        String param = "EURO";

        mockMvc.perform(post("/card/new")
                        .contentType("application/json")
                        .param("currency", param))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

//    @Test
//    @Order(2)
//    void mustContainsIdEquals1AndCurrencyEqualsEuro() throws Exception {
//        mockMvc.perform(get("/card/{id}", "1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.balance").isNumber())
//                .andExpect(jsonPath("$.cvc").exists())
//                .andExpect(jsonPath("$.currency").value("EURO"));
//
//    }
//
//    @Test
//    @Order(3)
//    void depositOperationMustHaveResultEquals500() throws Exception {
//        String param = "EURO";
//
//        mockMvc.perform(post("/card/new")
//                        .contentType("application/json")
//                        .param("currency", param))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNumber());
//
//        mockMvc.perform(post("/card/{id}/deposit", "1")
//                        .contentType("application/json")
//                        .param("amount", "500"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/card/{id}", "1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.balance").value(500.0));
//    }
//
//    @Test
//    @Order(4)
//    void withdrawOperationMustHaveResultEquals250() throws Exception {
//        mockMvc.perform(post("/card/{id}/withdraw", "1")
//                        .contentType("application/json")
//                        .param("amount", "250"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/card/{id}", "1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.balance").value(250.0));
//    }
//
//    @Test
//    @Order(5)
//    void parallelDeposit() throws Exception {
//        String param = "DOLLAR";
//
//        mockMvc.perform(post("/card/new")
//                        .contentType("application/json")
//                        .param("currency", param))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNumber());
//
//        List<Callable<ResultActions>> deposits = new ArrayList<>();
//
//        Callable<ResultActions> callable = () ->
//                mockMvc.perform(post("/card/{id}/deposit", "2")
//                                .contentType("application/json")
//                                .param("amount", "500"))
//                        .andExpect(status().isOk());
//
//        for (int i = 0; i < 1000; i ++) {
//            deposits.add(callable);
//        }
//
//        ExecutorService executor = Executors.newFixedThreadPool(8);
//
//        executor.invokeAll(deposits);
//
//        mockMvc.perform(get("/card/{id}/balance", "2"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}
