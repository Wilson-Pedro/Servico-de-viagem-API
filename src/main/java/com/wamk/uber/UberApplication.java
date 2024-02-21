package com.wamk.uber;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@SpringBootApplication
public class UberApplication implements CommandLineRunner{

//	private final UsuarioRepository usuarioRepository;
//
//	private final CarroRepository carroRepository;
//
//	private final ViagemRepository viagemRepository;
//
//	public UberApplication(UsuarioRepository usuarioRepository, CarroRepository carroRepository, ViagemRepository viagemRepository) {
//		this.usuarioRepository = usuarioRepository;
//		this.carroRepository = carroRepository;
//		this.viagemRepository = viagemRepository;
//	}

	public static void main(String[] args) {
		SpringApplication.run(UberApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
//		Passageiro p1 = new Passageiro(null, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
//		Passageiro p2 = new Passageiro(null, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
//		Passageiro p3 = new Passageiro(null, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
//		
//		Motorista m1 = new Motorista(null, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
//		Motorista m2 = new Motorista(null, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
//		Motorista m3 = new Motorista(null, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
//		
//		Carro c1 = new Carro(null, "Fiat", 2022, "JVF-9207", m1);
//		Carro c2 = new Carro(null, "Chevrolet", 2022, "FFG-0460", m2);
//		Carro c3 = new Carro(null, "Forger", 2022, "FTG-0160", m3);
//		
//		Viagem v1 = new Viagem(null, 
//				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
//				"10 minutos", p1, m1, FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
//		
//		usuarioRepository.saveAll(Arrays.asList(p1, p2, p3, m1, m2, m3));
//		carroRepository.saveAll(List.of(c1, c2, c3));
//		viagemRepository.saveAll(List.of(v1));
		
	}

}
