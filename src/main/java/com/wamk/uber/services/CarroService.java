package com.wamk.uber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wamk.uber.repositories.CarroRepository;

@Service
public class CarroService {

	@Autowired
	private CarroRepository carroRepository;
}
