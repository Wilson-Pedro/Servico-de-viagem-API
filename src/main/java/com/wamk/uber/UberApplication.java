package com.wamk.uber;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.SolicitarViagem;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.SolicitarViagemRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@SpringBootApplication
public class UberApplication implements CommandLineRunner{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CarroRepository carroRepository;
	
	@Autowired
	private SolicitarViagemRepository solicitarViagemRepository;
	
	@Autowired
	private ViagemRepository viagemRepository;

	public static void main(String[] args) {
		SpringApplication.run(UberApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Carro c1 = new Carro(null, "Fiat", 2022, "JVF-9207");
		
		Passageiro p1 = new Passageiro(null, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO);
		
		Motorista m1 = new Motorista(null, "Pedro", "982349876", TipoUsuario.MOTORISTA, c1);
		
		SolicitarViagem sv1 = new SolicitarViagem(null, 
				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
				p1.getNome(), new BigDecimal(18), FormaDePagamento.DEBITO);
		
		Viagem v1 = new Viagem(null, 
				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
				"20min", p1,(Motorista) m1, FormaDePagamento.PIX);
		
		carroRepository.saveAll(Arrays.asList(c1));
		usuarioRepository.saveAll(Arrays.asList(p1, m1));
		solicitarViagemRepository.saveAll(Arrays.asList(sv1));
		viagemRepository.saveAll(Arrays.asList(v1));
		
	}

}
