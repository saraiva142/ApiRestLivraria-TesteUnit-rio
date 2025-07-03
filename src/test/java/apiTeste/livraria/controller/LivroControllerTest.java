package apiTeste.livraria.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import apiTeste.livraria.entity.Livro;
import apiTeste.livraria.service.LivroService;
import apiTeste.livraria.service.exception.EntityNotFound;
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

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class createLivro {

        @Test
        @DisplayName("Should create livro with success")
        void createLivroWithSuccess() throws Exception {
            //Arrange
            Long id = 1L;
            Livro livro = new Livro();
            livro.setId(id);
            livro.setNome("Livro Java");

            Livro savedLivro = new Livro();
            savedLivro.setId(id);
            savedLivro.setNome("Livro Java");

            when(livroService.create(any(Livro.class))).thenReturn(savedLivro);

            //Act & Assert
            mockMvc.perform(post("/livros")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(livro)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.nome").value("Livro Java"));

            verify(livroService, times(1)).create(any(Livro.class));

        }
    }

    @Nested
    class deleteLivro {
        @Test
        @DisplayName("Should delete livro with success by id")
        void deleteLivroWithSuccessBYId() throws Exception {
            //Arrange
            Long id = 1L;

            Livro livro = new Livro();
            livro.setId(id);

            doNothing().when(livroService).delete(id);

            //Act & Assert
            mockMvc.perform(delete("/livros/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(livroService, times(1)).delete(id);
        }
    }

    @Nested
    class getIdLivro {

        @Test
        @DisplayName("Should get livro by id with success")
        void getIdLivroWithSuccess() throws Exception {
            //Arrange
            Long id = 1L;

            Livro livro = new Livro();
            livro.setId(id);
            livro.setNome("Livro Java");

            when(livroService.getId(id)).thenReturn(livro);

            //Act & Assert
            mockMvc.perform(get("/livros/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.nome").value("Livro Java"));

            verify(livroService, times(1)).getId(id);
        }

        @Test
        @DisplayName("Should return 404 when livro not found by id")
        void getByIdLivroWhenIDNotFound() throws Exception {
            //Arrange
            Long id = 99L;

            when(livroService.getId(id)).thenThrow(new EntityNotFound("Livro de ID " + id + " não encontrado."));

            //Act & Assert
            mockMvc.perform(get("/livros/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Livro de ID " + id + " não encontrado."));

            verify(livroService, times(1)).getId(id);
        }
    }

    @Nested
    class getAllLivros {

        @Test
        @DisplayName("Should get all livros with success")
        void getAllLivrosWithSuccess() throws Exception {
            //Arrange
            Long id = 1L;
            Livro livro1 = new Livro();
            livro1.setId(id);
            livro1.setNome("Bibliografia do Arnold Schwarzenegger");

            Livro livro2 = new Livro();
            livro2.setId(id);
            livro2.setNome("Bibliografia do Bruce Lee");

            List<Livro> livros = Arrays.asList(livro1, livro2);

            when(livroService.getAll()).thenReturn(livros);

            //Act & Assert
            mockMvc.perform(get("/livros")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(id))
                    .andExpect(jsonPath("$[0].nome").value("Bibliografia do Arnold Schwarzenegger"))
                    .andExpect(jsonPath("$[1].id").value(id))
                    .andExpect(jsonPath("$[1].nome").value("Bibliografia do Bruce Lee"));

            assertEquals(livro1.getNome(), livros.get(0).getNome());
            assertEquals(livro1.getId(), livros.get(0).getId());
            assertEquals(livro2.getNome(), livros.get(1).getNome());
            assertEquals(livro2.getId(), livros.get(1).getId());
            verify(livroService, times(1)).getAll();

        }
    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }
}