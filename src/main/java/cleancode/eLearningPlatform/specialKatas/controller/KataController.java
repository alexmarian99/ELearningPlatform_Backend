package cleancode.eLearningPlatform.specialKatas.controller;

import cleancode.eLearningPlatform.specialKatas.model.Kata;
import cleancode.eLearningPlatform.specialKatas.service.KataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/katas")
public class KataController {

    private final KataService kataService;

    @GetMapping
    public List<Kata> getAllKatas() {
        return kataService.findAllKatas();
    }

    @PostMapping
    public Kata saveOneKata(@RequestBody Kata kata){
        return kataService.saveKata(kata);
    }

}
