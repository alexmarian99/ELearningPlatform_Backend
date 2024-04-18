package cleancode.eLearningPlatform.modulesAndLessons.controller;

import cleancode.eLearningPlatform.auth.model.Response;
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
    public Week saveWeeks(@RequestBody Week week, @RequestHeader (name = "Authorization") String authHeader) {
        return weekService.saveWeek(week, authHeader);
    }

    @DeleteMapping
    public String deleteWeek(@RequestParam (name="weekId") int weekId,
                             @RequestHeader (name = "Authorization") String authHeader){
        return weekService.deleteWeekById(weekId , authHeader);
    }

    @PutMapping("/{weekId}")
    public Week updateWeek(@PathVariable int weekId,
                           @RequestBody Week updatedWeek ,
                           @RequestHeader (name = "Authorization") String authHeader){
        return weekService.updateWeek(weekId,updatedWeek, authHeader);
    }

    @PatchMapping("/permissions")
    public Week updatePermissionToWeek(@RequestParam (name = "weekId") int weekId,
                                       @RequestParam (name = "userId") int userId,
                                       @RequestHeader (name = "Authorization") String authHeader){
        return weekService.updatePermissionToWeek(weekId, userId , authHeader);
    }
}
