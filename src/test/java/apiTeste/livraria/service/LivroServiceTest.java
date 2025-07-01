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

    @Nested
    class getAll {
        @Test
        @DisplayName("Sould return all livros successfully")
        void getAllLivrosWithSuccess() {
            //Arrange
            Livro livro1 = new Livro();
            livro1.setId(1L);
            livro1.setNome("Livro 1");

            Livro livro2 = new Livro();
            livro2.setId(1L);
            livro2.setNome("Livro 2");

            List<Livro> livros = Arrays.asList(livro1, livro2);

            when(livroRepository.findAll()).thenReturn(livros);

            //Act
            List<Livro> result = livroService.getAll();

            //Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(livros, result);
            verify(livroRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when there are no have livros")
        void getAllLivrosIsEmptyWhenNoHaveLivros() {
            //Arrange
            List livros = new ArrayList();

            when(livroRepository.findAll()).thenReturn(livros);

            //Act
            List<Livro> result = livroService.getAll();

            //Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(livroRepository, times(1)).findAll();
        }
    }

    @Nested
    class update {
        @Test
        @DisplayName("Should update livro with success")
        void updateLivroWithSuccessfully() {
            //Arrange
            Long id = 1L;

            Livro livroOld = new Livro();
            livroOld.setNome("Arnold Schwarzenegger");
            livroOld.setId(id);

            Livro livroNew = new Livro();
            livroNew.setNome("Squibiridibi");
            livroNew.setId(id);

            Livro livroUpdated = new Livro();
            livroUpdated.setNome("Squibiridibi");
            livroUpdated.setId(id);

            when(livroRepository.findById(id)).thenReturn(Optional.of(livroOld));
            when(livroRepository.save(any(Livro.class))).thenReturn(livroUpdated);

            //Act
            Livro result = livroService.update(livroNew);

            //Assert
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals("Squibiridibi", result.getNome());
            verify(livroRepository, times(1)).findById(id);
        }


    }


}