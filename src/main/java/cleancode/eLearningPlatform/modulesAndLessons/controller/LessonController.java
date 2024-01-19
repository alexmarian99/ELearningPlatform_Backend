package cleancode.eLearningPlatform.modulesAndLessons.controller;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public List<Lesson> findAllLessons(){
        return lessonService.findAllLessons();
    }

    @PostMapping()
    public Lesson saveLesson(@RequestParam (name = "moduleId") Integer moduleId, @RequestBody Lesson lesson){
        return lessonService.saveLesson(moduleId, lesson);
    }
}
