package cleancode.eLearningPlatform.modulesAndLessons.controller;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public List<Lesson> findAllLessons() {
        return lessonService.findAllLessons();
    }


    @GetMapping("/findById/{lessonId}")
    public Lesson findLessonById(@PathVariable int lessonId){
        return lessonService.findLessonById(lessonId);
    }
    @GetMapping("/{weekId}")
    public List<Lesson> findAllLessonsByWeekId(@PathVariable int weekId) {
        return lessonService.findLessonByWeekId(weekId);
    }

    @PostMapping()
    public Lesson saveLesson(@RequestBody Lesson lesson,
                             @RequestHeader (name = "Authorization") String authHeader) {
        return lessonService.saveLesson(lesson, authHeader);
    }

    @DeleteMapping()
    public String deleteLesson (@RequestParam (name = "lessonId") Integer lessonId,
                                @RequestParam (name = "weekId") Integer weekId,
                                @RequestHeader (name = "Authorization") String authHeader){
        return lessonService.deleteLesson(lessonId, weekId, authHeader);
    }

    @PutMapping("/{lessonId}")
    public Lesson updateLesson(@PathVariable int lessonId,
                               @RequestBody Lesson updatedLesson,
                               @RequestHeader (name = "Authorization") String authHeader){
        return lessonService.updateLesson(lessonId,updatedLesson, authHeader);
    }
}
