package cleancode.eLearningPlatform.modulesAndLessons.controller;

import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.service.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weeks")
public class WeekController {
    private final WeekService weekService;

    @GetMapping("/{moduleId}")
    public List<Week> findWeeksByModuleId(@PathVariable int moduleId) {
        return weekService.findAllWeeksByModuleId(moduleId);
    }

    @PostMapping
    public Week saveWeeks(@RequestBody Week week) {
        return weekService.saveWeek(week);
    }
}
