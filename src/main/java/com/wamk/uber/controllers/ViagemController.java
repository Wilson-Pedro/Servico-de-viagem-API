package com.wamk.uber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.services.ViagemService;

@RestController
@RequestMapping("/carros")
public class ViagemController {

	@Autowired
	private ViagemService viagemService;
}
