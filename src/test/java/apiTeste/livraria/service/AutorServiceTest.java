package apiTeste.livraria.service;

import apiTeste.livraria.entity.Autor;
import apiTeste.livraria.repository.AutorRepository;
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
class AutorServiceTest {

    // Uma das dependências lá no service é o AutorRepository, então temos que mockar ele

    @Mock
    private AutorRepository autorRepository;

    @Autowired
    @InjectMocks
    private AutorService autorService;

//    @BeforeEach
//    void setup(){
//        MockitoAnnotations.initMocks(this);
//    }


    @Nested
    class CreateAutor {

        @Test
        @DisplayName("Should create autor with success")
        void createAutorWihtSuccess() {
            //Arrange
            Autor autorToSave = new Autor();
            autorToSave.setNome("Michael Sullivan");

            Autor savedAutor = new Autor();
            savedAutor.setId(1L);
            savedAutor.setNome("Michael Sullivan");

            when(autorRepository.save(autorToSave)).thenReturn(savedAutor);

            //Act
            Autor result = autorService.create(autorToSave);

            //Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Michael Sullivan", result.getNome());
            verify(autorRepository, Mockito.times(1)).save(autorToSave);
        }
    }

    @Nested
    class deleteAutor {
        @Test
        @DisplayName("Shoul delete by id with success")
        void deleteAutorByIdWithSuccess() {
            //Arrange
            Long autorIdToDelete = 1L;

            doNothing().when(autorRepository).deleteById(autorIdToDelete);

            //Act
            autorService.delete(autorIdToDelete);

            //Assert
            verify(autorRepository, times(1)).deleteById(autorIdToDelete);
        }

    }

    @Nested
    class getId {

        @Test
        @DisplayName("Should return autor by id with success")
        void getAutorByIdWithSuccess() {
            //Arrange
            Long id = 1L;
            Autor autorExcepted = new Autor();
            autorExcepted.setId(id);
            autorExcepted.setNome("Joao");

            when(autorRepository.findById(id)).thenReturn(Optional.of(autorExcepted));

            //Act
            Autor resultado = autorService.getId(id);

            //Assert
            assertNotNull(resultado);
            assertEquals(autorExcepted.getId(), resultado.getId());
            assertEquals(autorExcepted.getNome(), resultado.getNome());
            verify(autorRepository, times(1)).findById(id);

        }

        @Test
        @DisplayName("Should return error when autor not found")
        void getAutorByIdWithErrorWhenNotFound() {
            //Arrange
            Long id = 99L;

            when(autorRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            EntityNotFound exception = assertThrows(EntityNotFound.class, () -> {
                autorService.getId(id);
            });
            assertEquals("Autor de ID " + id + " não encontrado!", exception.getMessage());
            verify(autorRepository, times(1)).findById(id);
        }
    }

    @Nested
    class getAll {
        @Test
        @DisplayName("Should return all autores with success")
        void getAllAutoresWithSuccess() {
            //Arrange
            Autor autor1 = new Autor(1L, "Arnold Schwarzenegger");
            Autor autor2 = new Autor(1L, "Squibiridibi");
            List<Autor> autoresExpected = Arrays.asList(autor1, autor2);

            when(autorRepository.findAll()).thenReturn(autoresExpected);

            //Act
            List<Autor> result = autorService.getAll();

            //Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(autoresExpected, result);
            verify(autorRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when there are no have autores")
        void getAllAutoresIsEmptyWhenNoHaveAutor() {
            //Arrange
            List<Autor> emptyList = Arrays.asList();

            when(autorRepository.findAll()).thenReturn(emptyList);

            //Act
            List<Autor> result = autorService.getAll();

            //Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(autorRepository, times(1)).findAll();
        }

    }

    @Nested
    class update {
        @Test
        @DisplayName("Should update Autor with success!")
        void updateAutorWithSuccess() {
            //Arrange
            Long id = 1L;
            Autor autor = new Autor(id, "Antigo");
            Autor newAutor = new Autor(id, "Novo");
            Autor updatedAutor = new Autor(id, "Novo");

            when(autorRepository.findById(id)).thenReturn(Optional.of(autor));
            when(autorRepository.save(any(Autor.class))).thenReturn(updatedAutor);

            //Act
            Autor result = autorService.update(newAutor);

            //Assert
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals("Novo", result.getNome());
            verify(autorRepository, times(1)).findById(id);

            ArgumentCaptor<Autor> autorCaptor = ArgumentCaptor.forClass(Autor.class);
            verify(autorRepository, times(1)).save(autorCaptor.capture());
            assertEquals("Novo", autorCaptor.getValue().getNome());
            assertEquals(id, autorCaptor.getValue().getId());
        }

        @Test
        @DisplayName("Should not update when Autor is empty")
        void notUpdateAutorWhenAutorIsEmpty() {
            //Arrange
            Long id = 99L;
            Autor autor = new Autor(id, "Nome");

            when(autorRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            EntityNotFound exception = assertThrows(EntityNotFound.class, () -> {
                autorService.update(autor);
            });

            assertEquals("Autor de ID " + id + " não encontrado!", exception.getMessage());
            verify(autorRepository, times(1)).findById(id);
            verify(autorRepository, never()).save(any(Autor.class));

        }

    }

}