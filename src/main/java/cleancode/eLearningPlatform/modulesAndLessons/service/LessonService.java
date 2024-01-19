package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson saveLesson(Integer moduleId, Lesson lesson){
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);

        if(optionalModule.isPresent()){
            Module module = optionalModule.get();
            lesson.setModule(module);
            return lessonRepository.save(lesson);
        }else{
            throw new RuntimeException("Module not found with ID : " + moduleId);
        }


    }
}
