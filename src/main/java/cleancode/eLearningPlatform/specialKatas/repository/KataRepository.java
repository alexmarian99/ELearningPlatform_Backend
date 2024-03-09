package cleancode.eLearningPlatform.specialKatas.repository;

import cleancode.eLearningPlatform.specialKatas.model.Kata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KataRepository extends JpaRepository<Kata, Integer> {
    Optional<Kata> findByTitleAndKataLink(String title, String kataLink);
}
