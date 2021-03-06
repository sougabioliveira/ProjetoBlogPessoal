package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	public Optional<Usuario>cadastraUsuario(Usuario usuario){
		
		//valida se o usuario já existe
		if(repository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();
			
		//criptografa a senha do usuario	
		usuario.setSenha (criptografarSenha(usuario.getSenha()));
		//salvo o usuario com a senha já criptografada no banco de dados
		return Optional.of(repository.save(usuario));	
	}
	
	public Optional<Usuario> atualizaUsuario(Usuario usuario){
		
		//valida se o usuario já existe
		if(repository.findByUsuario(usuario.getUsuario()).isPresent()) {
			//atualiza o usuario 
			return Optional.of(repository.save(usuario));	
		} else {
			return Optional.empty();	
		}		
	}
	
	private String criptografarSenha(String senha){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	};
	
	public Optional<UsuarioLogin> autenticaUsuario(Optional<UsuarioLogin> usuarioLogin){
		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());

				return usuarioLogin;
			}
		}
		
		return Optional.empty();
	}
	
	//ESSA PARTE FAZ O COMPARATIVO DA SENHA DIGITADA COM A SENHA DO BANCO DE DADOS	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	//AQUI GERA O BASIC TOKEN, DEPOIS DE TODAS AS VALIDAÇÕES DAREM CERTAS
	private String gerarBasicToken(String usuario, String senha) {
	
		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
	}
	}

