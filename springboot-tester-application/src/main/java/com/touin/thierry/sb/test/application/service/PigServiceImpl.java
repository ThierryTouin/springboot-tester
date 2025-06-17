package com.touin.thierry.sb.test.application.service;

import com.touin.thierry.sb.test.application.dto.PigDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PigServiceImpl implements PigService {

    @Override
    public List<PigDTO> getPigs() {
        List<PigDTO> pigs = List.of(
                new PigDTO(1L, "Babe"),
                new PigDTO(2L, "Napoleon"),
                new PigDTO(3L, "Snowball")
        );

        return pigs.stream()
                .map(p -> new PigDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }
}
