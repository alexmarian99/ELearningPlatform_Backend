package cleancode.eLearningPlatform.specialKatas.repository;

import cleancode.eLearningPlatform.specialKatas.enums.Category;
import cleancode.eLearningPlatform.specialKatas.model.Kata;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KataRepository extends JpaRepository<Kata, Integer> {
    Optional<Kata> findByTitleAndKataLink(String title, String kataLink);

    @Query("SELECT k FROM Kata k")
    List<Kata> getSomeKata(Pageable pageable);

    boolean existsByTitle(String title);

    @Query("SELECT k FROM Kata k WHERE k.level IN (:kataLevels)")
    List<Kata> findAllByLevel(List<Integer> kataLevels);

    @Query("SELECT k FROM Kata k " +
            "WHERE (:level IS NULL OR k.level = :level) " +
            "AND (:category IS NULL OR :category MEMBER OF k.category) " +
            "AND (:status IS NULL OR (:status = 'COMPLETED' AND :userId MEMBER OF k.completedByUsers) OR (:status = 'TO_DO' AND :userId NOT MEMBER OF k.completedByUsers))")
    List<Kata> findByFilters(@Param("level") Integer level,
                             @Param("category") Category category,
                             @Param("userId") Long userId,
                             @Param("status") String status,
                             Pageable pageable);

    @Query("SELECT COUNT(k) FROM Kata k " +
            "WHERE (:level IS NULL OR k.level = :level) " +
            "AND (:category IS NULL OR :category MEMBER OF k.category) " +
            "AND (:status IS NULL OR (:status = 'COMPLETED' AND :userId MEMBER OF k.completedByUsers) OR (:status = 'TO_DO' AND :userId NOT MEMBER OF k.completedByUsers))")
    Long countFindByFilters(@Param("level") Integer level,
                             @Param("category") Category category,
                             @Param("userId") Long userId,
                             @Param("status") String status);



}
