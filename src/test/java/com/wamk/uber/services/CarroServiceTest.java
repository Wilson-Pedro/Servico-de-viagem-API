package com.wamk.uber.services;

import com.wamk.uber.UnitTest;
import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.CarroMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.templates.CarroDTOTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CarroServiceTest extends UnitTest {

    @InjectMocks
    private CarroService carroService;

    @Mock
    private CarroRepository carroRepository;

    @Spy
    private CarroMapper carroMapper;

    @Captor
    private ArgumentCaptor<Carro> carroArgumentCaptor;

    @Test
    void deveSalvar() {
        final CarroDTO dto = CarroDTOTemplate.valido();

        Mockito.when(carroRepository.existsByPlaca(dto.getPlaca())).thenReturn(false);

        carroService.save(dto);

        Mockito.verify(carroRepository).save(carroArgumentCaptor.capture());

        final Carro carroCapturado = carroArgumentCaptor.getValue();
        Assertions.assertNotNull(carroCapturado);
    }

    @Test
    void naoDeveSalvarQuandoCarroJaExistir() {

        final CarroDTO dto = CarroDTOTemplate.valido();

        Mockito.when(carroRepository.existsByPlaca(dto.getPlaca())).thenReturn(true);

        assertThrows(PlacaExistenteException.class, () -> carroService.save(dto));
    }
}
