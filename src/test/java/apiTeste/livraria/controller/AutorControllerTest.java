package apiTeste.livraria.controller;

import static org.junit.jupiter.api.Assertions.*;
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Nested
    class getIdAutor {
        @Test
        @DisplayName("Should get autor by Id successfully with Get request 200")
        void getIdAutorWithSucess() throws Exception {
            //Arrange
            Long id = 1L;
            Autor autor = new Autor();
            autor.setId(id);
            autor.setNome("Arnold Schwarzenegger");

            when(autorService.getId(id)).thenReturn(autor);

            //Act & Assert
            mockMvc.perform(get("/autor/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)) // Simula uma requisição GET para /autor/{id}
                    .andExpect(status().isOk()) // Espera um status HTTP 200 OK
                    .andExpect(jsonPath("$.id").value(id)) // Verifica o ID no JSON de resposta
                    .andExpect(jsonPath("$.nome").value("Arnold Schwarzenegger")); // Verifica o nome no JSON de resposta
            verify(autorService, times(1)).getId(id);
        }

        @Test
        @DisplayName("Should return error when autor not found with Get request 404")
        void getIdAutorWithErrorWhenNotFound() throws Exception {
            //Arrange
            Long id = 99L;

            when(autorService.getId(id)).thenThrow(new apiTeste.livraria.service.exception.EntityNotFound("Autor de ID " + id + " não encontrado!"));

            //Act & Assert
            mockMvc.perform(get("/autor/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)) // Simula uma requisição GET para /autor/{id}
                    .andExpect(status().isNotFound()) // Espera um status HTTP 404 Not Found
                    .andExpect(jsonPath("$.message").value("Autor de ID " + id + " não encontrado!")); // Verifica a mensagem de erro no JSON de resposta
            verify(autorService, times(1)).getId(id);
        }
    }

    @Nested
    class getAllAutores {
        @Test
        @DisplayName("Should get all autores successfully with Get request 200")
        void getAllAutoresWithSuccess() throws Exception {
            //Arrange
            Long id = 1L;

            Autor autor1 = new Autor();
            autor1.setId(1L);
            autor1.setNome("Arnold Schwarzenegger");

            Autor autor2 = new Autor();
            autor2.setId(id);
            autor2.setNome("Squibiridibi");

            List<Autor> autores = Arrays.asList(autor1, autor2);

            when(autorService.getAll()).thenReturn(List.of(autor1, autor2));

            //Act & Assert

            mockMvc.perform(get("/autor") // Simula uma requisição GET para /autor
                    .contentType(MediaType.APPLICATION_JSON)) // Simula uma requisição GET para /autor
                    .andExpect(status().isOk()) // Espera um status HTTP 200 OK
                    .andExpect(jsonPath("$[0].id").value(1L)) // Verifica o ID do primeiro autor no JSON de resposta
                    .andExpect(jsonPath("$[0].nome").value("Arnold Schwarzenegger")) // Verifica o nome do primeiro autor no JSON de resposta
                    .andExpect(jsonPath("$[1].id").value(1L)) // Verifica o ID do segundo autor no JSON de resposta
                    .andExpect(jsonPath("$[1].nome").value("Squibiridibi")); // Verifica o nome do segundo autor no JSON de resposta

            assertNotNull(autores);
            assertEquals(2, autores.size());
            verify(autorService, times(1)).getAll(); // Verifica se o metodo getAll do Service foi chamado
        }

        @Test
        @DisplayName("Should return empty list when no autores found with Get request 200")
        void getAllAutoresWithEmptyList() throws Exception {
            //Arrange
            List<Autor> autores = Arrays.asList();

            when(autorService.getAll()).thenReturn(autores);

            //Act & Assert
            mockMvc.perform(get("/autor") // Simula uma requisição GET para /autor)
                    .contentType(MediaType.APPLICATION_JSON)) // Define o tipo de conteúdo como JSON
                    .andExpect(status().isOk()) // Espera um status HTTP 200 OK
                    .andExpect(jsonPath("$").isEmpty()); // Verifica se a lista retornada está vazia

            assertEquals(0, autores.size()); // Verifica se o tamanho da lista é 0
            assertTrue(autores.isEmpty());
            verify(autorService, times(1)).getAll(); // Verifica se o metodo getAll do Service foi chamado

        }

        @Nested
        class updateAutor {
            @Test
            @DisplayName("Should update autor successfully with Put request 200 by id")
            void updateAutorWithIdSucessfully() throws Exception {
                Long id = 1L;

                Autor autorOld = new Autor();
                autorOld.setId(id);
                autorOld.setNome("Arnold Schwarzenegger");

                Autor autorNew = new Autor();
                autorNew.setId(id);
                autorNew.setNome("Squibiridibi");

                Autor autorUpdated = new Autor();
                autorUpdated.setId(id);
                autorUpdated.setNome("Squibiridibi");

                when(autorService.getId(id)).thenReturn(autorOld);
                when(autorService.update(any(Autor.class))).thenReturn(autorUpdated);

                //Act & Assert
                mockMvc.perform(put("/autor/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autorNew)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id))
                        .andExpect(jsonPath("$.nome").value("Squibiridibi"));

                verify(autorService, times(1)).update(any(Autor.class));
                assertEquals("Squibiridibi", autorUpdated.getNome());
                assertTrue(autorUpdated.getId().equals(id));
            }



        }

    }



}