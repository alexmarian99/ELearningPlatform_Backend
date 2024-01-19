package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
