package com.wamk.uber.integracao.controllers;

import static com.wamk.uber.LoginUniversal.TOKEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UserRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.ViagemService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViagemControllerTestI {
	
	private static String VIAGEM_ENDPOINT = "/viagens";

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	ViagemService viagemService;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	List<Usuario> usuarios = List.of(
			new Passageiro(null, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(null, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(null, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(null, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(null, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(null, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);
	
	List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207",(Motorista) usuarios.get(3)),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460",(Motorista) usuarios.get(4)),
			new Carro(3L, "Forger", 2022, "FTG-0160",(Motorista) usuarios.get(5))
	);
	
	Viagem viagem = new Viagem(null, 
			"Novo Castelo - Rua das Goiabas 1010", 
			"Pará - Rua das Maçãs", 
			"10 minutos", (Passageiro)usuarios.get(0), (Motorista)usuarios.get(3), 
			FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
	
	@Test
	@Order(1)
	void deveDeletarTudo() {
		userRepository.deleteAll();
		viagemRepository.deleteAll();
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
	}

	@Test
	@Order(3)
	void deveRegistraUsuarioParaLoginComSucesso() {
		RegistroDTO registroDTO = new RegistroDTO("pedro", "34567", UserRole.ADMIN);
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(registroDTO.getPassword());
		
		assertNotNull(encryptedPassword);
		assertNotEquals(encryptedPassword, registroDTO.getPassword());
		
		User newUser = new User(registroDTO.getLogin(), encryptedPassword, registroDTO.getRole());
		
		userRepository.save(newUser);
	}
	
	@Test
	@Order(4)
	void deveRealizarLoginComSucesso() {
		AuthenticationDTO dto = new AuthenticationDTO("pedro", "34567");
		var usernamePassowrd = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
		var auth = this.authenticationManager.authenticate(usernamePassowrd);
		var token = this.tokenService.generateToken((User) auth.getPrincipal());
		
		assertNotNull(token);
		TOKEN = token;
	}
	
	@Test
	@Order(5)
	void deveBuscarTodasAsViagensComSucesso() throws Exception {
		
		mockMvc.perform(get(VIAGEM_ENDPOINT)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(6)
	void deveRegistrarUmaViagemComSucesso() throws Exception {
		
		usuarioRepository.save(usuarios.get(1));
		usuarioRepository.save(usuarios.get(4));
		
		Long passgaeiroId = usuarioRepository.findAll().get(0).getId();
		Long motoristaId = usuarioRepository.findAll().get(1).getId();
		
		ViagemInputDTO inputDto = new ViagemInputDTO( 
						"Novo Castelo - Rua das Goiabas 1010", 
						"Pará - Rua das Maçãs", 
						"10 minutos", passgaeiroId, motoristaId, FormaDePagamento.PIX.getDescricao());
		
		String jsonRequest = objectMapper.writeValueAsString(inputDto);
		
		assertEquals(0, viagemRepository.count());
		
		mockMvc.perform(post(VIAGEM_ENDPOINT + "/")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.origem", equalTo("Novo Castelo - Rua das Goiabas 1010")))
				.andExpect(jsonPath("$.destino", equalTo("Pará - Rua das Maçãs")))
				.andExpect(jsonPath("$.tempoDeViagem", equalTo("10 minutos")))
				.andExpect(jsonPath("$.nomePassageiro", equalTo("Ana")))
				.andExpect(jsonPath("$.nomeMotorista", equalTo("Julia")))
				.andExpect(jsonPath("$.formaDePagamento", equalTo("PIX")))
				.andExpect(jsonPath("$.viagemStatus", equalTo("NAO_FINALIZADA")))
				.andReturn();
		
		assertEquals(1, viagemRepository.count());
		
	}
	
	@Test
	@Order(7)
	void deveBuscarViagemPorIdComSucesso() throws Exception {
		
		Long id = viagemService.findAll().get(0).getId();
		
		mockMvc.perform(get("/viagens/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(id.intValue())))
				.andExpect(jsonPath("$.origem", equalTo("Novo Castelo - Rua das Goiabas 1010")))
				.andExpect(jsonPath("$.destino", equalTo("Pará - Rua das Maçãs")))
				.andExpect(jsonPath("$.tempoDeViagem", equalTo("10 minutos")))
				.andExpect(jsonPath("$.nomePassageiro", equalTo("Ana")))
				.andExpect(jsonPath("$.nomeMotorista", equalTo("Julia")))
				.andExpect(jsonPath("$.formaDePagamento", equalTo("PIX")))
				.andExpect(jsonPath("$.viagemStatus", equalTo("NAO_FINALIZADA")));
	}
	
	@Test
	@Order(8)
	void deveAtualizarViagemComSucesso() throws Exception {
		ViagemInputDTO inputDto = new ViagemInputDTO( 
				"Novo Castelo - Rua das Goiabas 1010", 
				"Pará - Rua das Melancias", 
				"20 minutos", 2L, 5L, FormaDePagamento.PAYPAL.getDescricao());
		
		Long id = viagemService.findAll().get(0).getId();
		
		Viagem viagem = viagemService.findById(id);
		
		assertNotEquals(viagem.getDestino(), inputDto.getDestino());
		
		String josnRequest = objectMapper.writeValueAsString(inputDto);
		
		mockMvc.perform(put(VIAGEM_ENDPOINT + "/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(josnRequest))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.destino", equalTo("Pará - Rua das Melancias")))
				.andReturn();
	}
	
	@Test
	@Order(9)
	void deveDeletarViagemComSucesso() throws Exception {
		
		usuarioRepository.save(usuarios.get(0));
		usuarioRepository.save(usuarios.get(3));
		viagemRepository.save(viagem);
		
		Long id = viagemService.findAll().get(1).getId();
		
		assertEquals(2, viagemRepository.count());
		
		mockMvc.perform(delete(VIAGEM_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(1, viagemRepository.count());
		
	}
	
	@Test
	@Order(10)
	void deveCancelarViagemComSucesso() throws Exception {
		
		Long id = viagemService.findAll().get(0).getId();
		
		assertEquals(1, viagemRepository.count());
		
		mockMvc.perform(delete(VIAGEM_ENDPOINT + "/{id}/cancelar", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(0, viagemRepository.count());
		
	}

	@Test
	@Order(11)
	void deveFinalizarViagemComSucesso() throws Exception {
		
		usuarioRepository.save(usuarios.get(2));
		usuarioRepository.save(usuarios.get(5));
		
		Passageiro p = (Passageiro) usuarioRepository.findById(usuarios.get(2).getId()).get();
		
		Motorista m = (Motorista) usuarioRepository.findById(usuarios.get(5).getId()).get();
		
		Viagem viagem = new Viagem(null, 
				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
				"10 minutos", p, m, FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
		

		viagemRepository.save(viagem);
		
		Long id = viagem.getId();
		
		assertEquals(ViagemStatus.NAO_FINALIZADA, viagem.getViagemStatus());
		
		mockMvc.perform(patch(VIAGEM_ENDPOINT + "/{id}/finalizar", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		Viagem viagemEsperada = viagemService.findById(id);
		
		assertEquals(ViagemStatus.FINALIZADA, viagemEsperada.getViagemStatus());
	}
	
	@Test
	@Order(12)
	void devePaginarUmaListaDeViagensComSucesso() throws Exception {
		
		var viagens = viagemService.findAll();
		Page<Viagem> page = new PageImpl<>(viagens, PageRequest.of(0, 10), viagens.size());
		
		mockMvc.perform(get(VIAGEM_ENDPOINT + "/pages")
				.header("Authorization", "Bearer " + TOKEN))
		 		.andExpect(status().isOk())
		 		.andReturn();
		
		assertEquals(page.getContent().size(), viagens.size());
		
	}
}