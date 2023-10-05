package com.wamk.uber.templates;

import com.wamk.uber.entities.Carro;

public class CarroTemplate {

    public static Carro valido() {
        var dto = new Carro();
        dto.setAno(2020);
        dto.setModelo("Fusca");
        dto.setPlaca("ABC-1234");
        return dto;
    }
}
