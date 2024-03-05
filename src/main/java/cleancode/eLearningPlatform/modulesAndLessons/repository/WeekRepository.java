package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WeekRepository extends JpaRepository<Week, Integer> {
    List<Week> findAllByModuleIdOrderByNumber(int moduleId);

    @Query("SELECT w.lessons FROM Week w WHERE w.id = :weekId")
    List<Lesson> getLessonsByWeekId(Integer weekId);

    @Query("SELECT w.module.weeks FROM Week w WHERE w.id = :weekId")
    List<Week> getRestOfWeeks(Integer weekId);

    @Query("SELECT w.module.id FROM Week w WHERE w.id = :weekId")
    Integer getModuleIdFromWeek(Integer weekId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Week l WHERE l.id = :weekId")
    void deleteWeekById(@Param("weekId") int weekId);


}
