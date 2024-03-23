package cleancode.eLearningPlatform.specialKatas.repository;

import cleancode.eLearningPlatform.specialKatas.enums.Category;
import cleancode.eLearningPlatform.specialKatas.model.Kata;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KataRepository extends JpaRepository<Kata, Integer> {
    Optional<Kata> findByTitleAndKataLink(String title, String kataLink);

    @Query("SELECT k FROM Kata k")
    List<Kata> getSomeKata(Pageable pageable);

    boolean existsByTitle(String title);


    @Query("SELECT k FROM Kata k WHERE k.level IN (:kataLevels)")
    List<Kata> findAllByLevel(List<Integer> kataLevels);

    List<Kata> findByCategoryAndLevel(Category category, int kataLevel, Pageable pageable);
    Integer countByCategoryAndLevel(Category category, int kataLevel);


    List<Kata> findByCategory(Category category, Pageable pageable);
    Integer countByCategory(Category category);

    List<Kata> findByLevel(Integer kataLevel, Pageable pageable);
    Integer countByLevel(Integer kataLevel);
}
