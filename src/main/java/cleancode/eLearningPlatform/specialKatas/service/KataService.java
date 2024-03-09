package cleancode.eLearningPlatform.specialKatas.service;

import cleancode.eLearningPlatform.specialKatas.model.Kata;
import cleancode.eLearningPlatform.specialKatas.repository.KataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KataService {

    private final KataRepository kataRepository;

    public List<Kata> findAllKatas(){
        return kataRepository.findAll();
    }

    public Kata saveKata(Kata kata) {

        Optional<Kata> existingKata = kataRepository.findByTitleAndKataLink(kata.getTitle(), kata.getKataLink());
        if (existingKata.isPresent()) {

            return existingKata.get();
        } else {
            // Kata does not exist, save it
            return kataRepository.save(kata);
        }
    }

    public boolean kataExists(String title, String kataLink) {
        Optional<Kata> existingKata = kataRepository.findByTitleAndKataLink(title, kataLink);
        return existingKata.isPresent();
    }
}
