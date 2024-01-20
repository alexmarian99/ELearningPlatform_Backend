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

    @GetMapping("/{moduleId}")
    public List<Lesson> findModuleLessons(@PathVariable Integer moduleId){
        return lessonService.findModuleLessons(moduleId);
    }

    @PostMapping()
    public Lesson saveLesson( @RequestBody Lesson lesson){
        return lessonService.saveLesson( lesson);
    }
}
