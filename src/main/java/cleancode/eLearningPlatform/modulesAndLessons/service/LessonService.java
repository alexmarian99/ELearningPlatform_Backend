package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findModuleLessons(Integer moduleId) {
        return lessonRepository.findAllByModuleId(moduleId).orElse(null);
    }

   public Lesson saveLesson( Lesson lesson){

    return lessonRepository.save(lesson);
    }


}
