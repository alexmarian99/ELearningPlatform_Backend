package cleancode.eLearningPlatform.auth.repository;



import cleancode.eLearningPlatform.auth.model.CompletedStuff;
import cleancode.eLearningPlatform.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u.completedLessons FROM User u WHERE u.id = :userId")
    List<Integer> getJustLessons(Integer userId);

    @Query("SELECT u.completedWeeks FROM User u WHERE u.id = :userId")
    List<Integer> getJustWeeks(Integer userId);

//    @Query("SELECT u.completedLessons, u.completedWeeks FROM User u WHERE u.id = :userId")
//    List<Object[]> getLessonsAndWeeks(Integer userId);
//
//    @Query("SELECT NEW cleancode.eLearningPlatform.auth.model.CompletedStuff(u.completedLessons, u.completedWeeks) FROM User u WHERE u.id = :userId")
//    CompletedStuff getCompletedStuffResult(Integer userId);





}
