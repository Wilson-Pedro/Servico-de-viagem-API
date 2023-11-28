package com.wamk.uber.dtos.mapper;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;

public class CarroMapper {

	public static CarroDTO toDTO(Carro carro) {
		if(carro == null) {
			return null;
		}
		var carroDTO = new CarroDTO();
		carroDTO.setId(carro.getId());
		carroDTO.setAno(carro.getAno());
		carroDTO.setModelo(carro.getModelo());
		carroDTO.setPlaca(carro.getPlaca());
		return carroDTO;
	}
	
	public static Carro toEntity(CarroDTO carroDTO) {
		if(carroDTO == null) {
			return null;
		}
		var carro = new Carro();
		if(carroDTO.getId() != null) {
			carro.setId(carroDTO.getId());
		}
		carro.setAno(carroDTO.getAno());
		carro.setModelo(carroDTO.getModelo());
		carro.setPlaca(carroDTO.getPlaca());
		return carro;
	}
}
