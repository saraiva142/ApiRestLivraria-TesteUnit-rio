package apiTeste.livraria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import apiTeste.livraria.controller.AutorController; // Assumindo que você tem um AutorController
import apiTeste.livraria.entity.Autor;
import apiTeste.livraria.service.AutorService;

@WebMvcTest(AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc; //Dependência para simular requisições HTTP

    @MockitoBean
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper; //Para converter objetos em JSON e vice-versa

    @Nested
    class creteAutor {
        @Test
        @DisplayName("Should create autor successfully with Post request 201")
        void createAutorPostWithSuccess() throws Exception {
            //Arrange
            // Aqui deve criar um objeto Autor e definir os valores necessários
            Long id = 1L;
            Autor autorToCreate = new Autor();
            autorToCreate.setId(id);
            autorToCreate.setNome("Arnold Schwarzenegger");

            Autor savedAutor = new Autor();
            savedAutor.setId(id);
            savedAutor.setNome("Arnold Schwarzenegger");

            // Aqui deve simular o comportamento do serviço para retornar o autor criado
            when(autorService.create(any(Autor.class))).thenReturn(savedAutor);

            // Act
            // Aqui deve simular uma requisição POST para o endpoint /autor
            // Assert
            // Aqui deve verificar se o status da resposta é 201 Created e se o corpo da resposta contém o autor criado
            // Act & Assert
            mockMvc.perform(post("/autor") // Simula uma requisição POST para /autores
                            .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                            .content(objectMapper.writeValueAsString(autorToCreate))) // Converte o objeto para JSON
                    .andExpect(status().isCreated()) // Espera um status HTTP 201 Created
                    .andExpect(jsonPath("$.id").value(1L)) // Verifica o ID no JSON de resposta
                    .andExpect(jsonPath("$.nome").value("Arnold Schwarzenegger")); // Verifica o nome no JSON de resposta

            verify(autorService, times(1)).create(any(Autor.class)); // Verifica se o método create do Service foi chamado
        }
    }

    @Nested
    class deleteAutor {
        @Test
        @DisplayName("Should delete autor successfully with Delete request 204")
        void deleteAutorByIdWithSuccess() throws Exception {
            //Arrange
            Long id = 1L;

            doNothing().when(autorService).delete(id);

            // Act & Assert
            mockMvc.perform(delete("/autor/{id}", id) // Simula uma requisição DELETE para /autor/{id}
                    .contentType(MediaType.APPLICATION_JSON)) // Define o tipo de conteúdo como JSON
                    .andExpect(status().isNoContent()); // Espera um status HTTP 204 No Content

            verify(autorService, times(1)).delete(id); // Verifica se o metodo delete do Service foi chamado com o ID correto
        }
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