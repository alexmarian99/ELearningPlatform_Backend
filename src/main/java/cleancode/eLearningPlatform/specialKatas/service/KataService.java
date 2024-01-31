package cleancode.eLearningPlatform.specialKatas.service;

import cleancode.eLearningPlatform.specialKatas.model.Kata;
import cleancode.eLearningPlatform.specialKatas.repository.KataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KataService {

    private final KataRepository kataRepository;

    public List<Kata> findAllKatas(){
        return kataRepository.findAll();
    }

    public Kata saveKata(Kata kata){
        return kataRepository.save(kata);
    }

}
