package apiTeste.livraria.service;

import apiTeste.livraria.entity.Livro;
import apiTeste.livraria.repository.LivroRepository;
import apiTeste.livraria.service.exception.EntityNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Autowired
    @InjectMocks
    private LivroService livroService;

    @Nested
    class createLivro{
        @Test
        @DisplayName("Should create livro successfully")
        void createLivroWithSuccess() {
            //Arrange
            Livro newLivro = new Livro();
            newLivro.setNome("Java Livro");

            Livro livroSaved = new Livro();
            livroSaved.setId(1L);
            livroSaved.setNome("Java Livro");

            when(livroRepository.save(newLivro)).thenReturn(livroSaved);

            //Act
            Livro result = livroService.create(newLivro);

            //Assert
            assertNotNull(result);
            assertEquals(1l, result.getId());
            assertEquals(livroSaved.getNome(), result.getNome());
            verify(livroRepository, times(1)).save(newLivro);
        }
    }

    @Nested
    class deleteLivro {
        @Test
        @DisplayName("Should delete livro with success")
        void deleteLivroWithSuccess() {
            //Arrange
            Long id = 1L;
            doNothing().when(livroRepository).deleteById(id);

            //Act
            livroService.delete(id);

            //Assert
            verify(livroRepository, times(1)).deleteById(id);
        }
    }

    @Nested
    class getId {
        @Test
        @DisplayName("Should get livro by Id successfully")
        void getLivroByIdWithSuccess() {
            //Arrange
            Long id = 1L;
            Livro livro = new Livro();
            livro.setNome("Java Livro");
            livro.setId(id);

            when(livroRepository.findById(id)).thenReturn(Optional.of(livro));

            //Act
            Livro result = livroService.getId(id);

            //Assert
            assertNotNull(result);
            assertEquals(livro.getId(), result.getId());
            assertEquals(livro.getNome(), result.getNome());
            verify(livroRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("Should throw EntityNotFound when livro not found by Id")
        void getLivroByIdWhenNotFound() {
            //Arrange
            Long id = 99l;

            when(livroRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            EntityNotFound exception = assertThrows(EntityNotFound.class, () -> {
                livroService.getId(id);
            });
            assertEquals("Livro de ID " + id + " não encontrado!", exception.getMessage());
            verify(livroRepository, times(1)).findById(id);

        }

    }


    @Test
    void getAll() {
    }

    @Test
    void update() {
    }
}