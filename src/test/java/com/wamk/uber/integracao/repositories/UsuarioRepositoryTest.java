package com.wamk.uber.integracao.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.wamk.uber.repositories.UsuarioRepository;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
