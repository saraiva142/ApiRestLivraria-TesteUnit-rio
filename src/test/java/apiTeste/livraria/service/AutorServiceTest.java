package apiTeste.livraria.service;

import apiTeste.livraria.entity.Autor;
import apiTeste.livraria.repository.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    // Uma das dependências lá no service é o AutorRepository, então temos que mockar ele

    @Mock
    private AutorRepository autorRepository;

    @Autowired
    @InjectMocks
    private AutorService autorService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Nested
    class createAutor {

        @Test
        @DisplayName("Should create autor with success")
        void createAutorWihtSuccess() {
            //Arrange
            Autor autorToSave = new Autor();
            autorToSave.setNome("Michael Sullivan");

            Autor savedAutor = new Autor();
            savedAutor.setId(1L);
            savedAutor.setNome("Michael Sullivan");

            Mockito.when(autorRepository.save(autorToSave)).thenReturn(savedAutor);

            //Act
            Autor result = autorService.create(autorToSave);

            //Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Michael Sullivan", result.getNome());
            Mockito.verify(autorRepository, Mockito.times(1)).save(autorToSave);
        }
    }

    @Nested
    class deleteAutor {
        @Test
        @DisplayName("Shoul delete by id with success")
        void deleteAutorByIdWithSuccess() {

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