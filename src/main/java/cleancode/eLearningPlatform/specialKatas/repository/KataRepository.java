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

////    ALL FILTER
    List<Kata> findByCategoryAndLevelAndCompletedByUsers(Category category, int kataLevel, long userId, Pageable pageable);
    Integer countByCategoryAndLevelAndCompletedByUsers(Category category, int kataLevel, long userId);


   // CATEGORY AND LEVEL
    List<Kata> findByCategoryAndLevel(Category category, int kataLevel, Pageable pageable);
    Integer countByCategoryAndLevel(Category category, int kataLevel);

    //  CATEGORY AND STATUS
    List<Kata> findByCategoryAndCompletedByUsers(Category category, long userId, Pageable pageable);
    Integer countByCategoryAndCompletedByUsers(Category category, long userId);

    // STATUS AND DIFICULTY

    List<Kata> findByLevelAndCompletedByUsers(int kataLevel, long userId);
    Integer countByLevelAndCompletedByUsers(int kataLevel, long userId);


 // CATEGORY
    List<Kata> findByCategory(Category category, Pageable pageable);
    Integer countByCategory(Category category);

    // LEVEL
    List<Kata> findByLevel(Integer kataLevel, Pageable pageable);
    Integer countByLevel(Integer kataLevel);

   //  USER
   List<Kata> findByCompletedByUsers(long userId);
   Integer countByCompletedByUsers(long userId);



//    @Query("SELECT k FROM Kata k WHERE" +
//            " (:category is null or k.category = :category) and " +
//            "(:category is null or k.level = :level) and" +
//            "(:userId is null or :userId IN (k.completedByUsers))")
//    List<Kata> findFilteredKatas(
//            @Param("category") Category category,
//            @Param("level") Integer level,
//            @Param("userId") Long userId
//    );
}
