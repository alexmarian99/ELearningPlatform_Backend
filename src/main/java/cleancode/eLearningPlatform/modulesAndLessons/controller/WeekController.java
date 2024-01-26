package cleancode.eLearningPlatform.modulesAndLessons.controller;

import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.service.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/week")
public class WeekController {
    private final WeekService weekService;

    @GetMapping
    public List<Week> findAllWeeks(){
        return weekService.findAllWeeks();
    }

    @PostMapping
    public Week saveWeeks(@RequestBody Week week){
        return weekService.saveWeek(week);
    }
}
