package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;


//este define que a classe é de teste, que vai rodar em uma porta aleatória a cada teste
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//cria uma instancia de teste que define que o ciclo de vida do teste irá respeitar o ciclo de vida da classe. Será executado e resetado, após o uso.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository repository;
	
	@BeforeAll
	void start() {
		
		repository.save(new Usuario(1L, "Maiar", "isadora@gmail.com","51 e pinga", "https://i.imgur.com/FETvs20.jpg","Normal"));
		repository.save(new Usuario(2L, "Michael", "michaeltrimundial@gmail.com","nunca fui rebaixado", "https://i.imgur.com/FETvs20.jpg","Normal"));
		repository.save(new Usuario(3L, "Brocco", "broco@gmail.com","broccolis", "https://i.imgur.com/FETvs20.jpg","Normal"));
	}
	
	//ISTO TUDO É UM TESTE, ele vai te retornar um usuario:
	@Test
	@DisplayName("Teste que retorna 1 usuario")
	//quando tem um "public" estamos criando uma função
	public void retornaUmUsuarioPeloUsuario() {
		// O optional é quando tem mais de uma resposta
		//O List é quando temos certeza de que há uma lista
		Optional<Usuario> usuario = repository.findByUsuario("isadora@gmail.com");
		System.out.println("usuario "+ usuario.get());
		assertTrue(usuario.get().getUsuario().equals("isadora@gmail.com"));
	}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void retornaTresUsuarios() {
		
		List<Usuario> listaDeUsuarios = repository.findAll();
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Maiar"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Michael"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Brocco"));
	}
	
	@Test
	@DisplayName("Retorna 1 usuario")
	public void retornaUmUsuarioPeloNome() {
		
		List<Usuario> listaDeUsuarios = repository.findAllByNomeContainingIgnoreCase("Maiar");
		assertEquals(1, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Maiar"));
	}
	
	@AfterAll
	public void end() {
		repository.deleteAll();
	}
}
