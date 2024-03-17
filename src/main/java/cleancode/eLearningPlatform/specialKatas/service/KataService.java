package cleancode.eLearningPlatform.specialKatas.service;

import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.specialKatas.model.Kata;
import cleancode.eLearningPlatform.specialKatas.repository.KataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KataService {

    private final KataRepository kataRepository;
    private final UserService userService;

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

@Modifying
@Transactional
    public Response deleteKata(int id) {

        kataRepository.deleteById(id);
        userService.removeKataFromAllUsers(id);
       return Response.builder().response("Kata deleted").build();
    }
}
