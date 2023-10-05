package com.wamk.uber.templates;

import com.wamk.uber.dtos.CarroDTO;

public class CarroDTOTemplate {

    public static CarroDTO valido() {
        var dto = new CarroDTO();
        dto.setAno(2020);
        dto.setModelo("Fusca");
        dto.setPlaca("ABC-1234");
        return dto;
    }
}
