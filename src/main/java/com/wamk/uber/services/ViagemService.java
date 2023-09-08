package com.wamk.uber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wamk.uber.repositories.ViagemRepository;

@Service
public class ViagemService {

	@Autowired
	private ViagemRepository viagemRepository;
}
