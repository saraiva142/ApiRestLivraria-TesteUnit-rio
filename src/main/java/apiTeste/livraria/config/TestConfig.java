package apiTeste.livraria.config;

import apiTeste.livraria.entity.Autor;
import apiTeste.livraria.entity.Livro;
import apiTeste.livraria.repository.AutorRepository;
import apiTeste.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorRepository autorRepository;

    @Override
    public void run(String... args) throws Exception{

        Autor a1 = new Autor(null, "Carlos");
        Autor a2 = new Autor(null, "Joao");

        autorRepository.saveAll(Arrays.asList(a1, a2));

        Livro l1 = new Livro(null, "Java Livro", a1);
        Livro l2 = new Livro(null, "Python Livro", a2);

        livroRepository.saveAll(Arrays.asList(l1, l2));
    }
}
