package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface WeekRepository extends JpaRepository<Week, Integer> {
    List<Week> findAllByModuleIdOrderByNumber(int moduleId);

    @Query("SELECT w.lessons FROM Week w WHERE w.id = :weekId")
    List<Lesson> getLessonsByWeekId(int weekId);

    @Query("SELECT w.id, w.number FROM Week w WHERE w.module.id = :moduleId")  // keep in mind to do this
    List<Object[]> getAllWeeksIdByModuleId(int moduleId);


    @Query("SELECT w.module.weeks FROM Week w WHERE w.id = :weekId")
    List<Week> getRestOfWeeks(Integer weekId);

    @Query("SELECT w.module.id FROM Week w WHERE w.id = :weekId")
    Integer getModuleIdFromWeek(Integer weekId);



}
