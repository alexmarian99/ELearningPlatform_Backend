package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekRepository extends JpaRepository<Week, Integer> {

}
