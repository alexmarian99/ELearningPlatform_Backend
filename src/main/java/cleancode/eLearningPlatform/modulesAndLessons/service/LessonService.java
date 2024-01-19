package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson saveLesson(Integer moduleId, Lesson lesson) {
        return lessonRepository.save(lesson);
    }
}
