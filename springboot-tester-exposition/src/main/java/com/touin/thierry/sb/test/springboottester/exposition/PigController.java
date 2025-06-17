package com.touin.thierry.sb.test.springboottester.exposition;

import com.touin.thierry.sb.test.application.dto.PigDTO;
import com.touin.thierry.sb.test.application.service.PigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PigController {

    private final PigService pigService;

    public PigController(PigService pigService) {
        this.pigService = pigService;
    }

    @GetMapping("/pigs")
    public List<PigDTO> getPigs() {
        return pigService.getPigs();
    }
}