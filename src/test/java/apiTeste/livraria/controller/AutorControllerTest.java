package apiTeste.livraria.controller;

import apiTeste.livraria.service.AutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc; //Dependência para simular requisições HTTP

    @Mock
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper; //Para converter objetos em JSON e vice-versa

    @Nested
    class creteAutor {
        @Test
        @DisplayName("Should create autor successfully with Post request 201")
        void createAutorPostWithSuccess() {
            //Arrange
        }

    }


    @Test
    void delete() {
    }

    @Test
    void getId() {
    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }
}