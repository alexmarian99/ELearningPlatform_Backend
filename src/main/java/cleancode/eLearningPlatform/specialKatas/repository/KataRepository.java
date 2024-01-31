package cleancode.eLearningPlatform.specialKatas.repository;

import cleancode.eLearningPlatform.specialKatas.model.Kata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KataRepository extends JpaRepository<Kata, Integer> {
}
