package com.wamk.uber;

import java.math.BigDecimal;
import java.util.List;

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

	private final UsuarioRepository usuarioRepository;

	private final CarroRepository carroRepository;
	
	private final SolicitarViagemRepository solicitarViagemRepository;

	private final ViagemRepository viagemRepository;

	public UberApplication(UsuarioRepository usuarioRepository, CarroRepository carroRepository,
			SolicitarViagemRepository solicitarViagemRepository, ViagemRepository viagemRepository) {
		this.usuarioRepository = usuarioRepository;
		this.carroRepository = carroRepository;
		this.solicitarViagemRepository = solicitarViagemRepository;
		this.viagemRepository = viagemRepository;
	}

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
		
		carroRepository.saveAll(List.of(c1));
		usuarioRepository.saveAll(List.of(p1, m1));
		solicitarViagemRepository.saveAll(List.of(sv1));
		viagemRepository.saveAll(List.of(v1));
		
	}

}
