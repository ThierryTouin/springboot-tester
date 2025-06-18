package com.touin.thierry.sb.test.application.service;

import com.touin.thierry.sb.test.application.dto.PigDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PigServiceImpl implements PigService {

private static final Logger LOGGER = LoggerFactory.getLogger(PigServiceImpl.class);

    @Override
    public List<PigDTO> getPigs() {
        List<PigDTO> pigs = List.of(
                new PigDTO(1L, "Babe"),
                new PigDTO(2L, "Napoleon"),
                new PigDTO(3L, "Snowball"),
                new PigDTO(4L, "Snowball18")
        );

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("$>>> pigs={}",pigs);
		}
        System.out.println(">>> pigs={}" + pigs);

        return pigs.stream()
                .map(p -> new PigDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }
}
