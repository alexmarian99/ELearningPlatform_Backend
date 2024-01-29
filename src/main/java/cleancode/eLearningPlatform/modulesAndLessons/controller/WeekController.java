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

    @GetMapping()
    public Week findWeekById(@RequestParam (name="weekId") int weekId){
        return weekService.findWeekById(weekId);
    }

    @GetMapping("/{moduleId}")
    public List<Week> findWeeksByModuleId(@PathVariable int moduleId) {
        return weekService.findAllWeeksByModuleId(moduleId);
    }

    @PostMapping
    public Week saveWeeks(@RequestBody Week week) {
        return weekService.saveWeek(week);
    }

    @DeleteMapping
    public String deleteWeek(@RequestParam (name="weekId") int weekId){
        return weekService.deleteWeekById(weekId);
    }

    @PutMapping("/{weekId}")
    public Week updateWeek(@PathVariable int weekId,@RequestBody Week updatedWeek){
        return weekService.updateWeek(weekId,updatedWeek);
    }
}
