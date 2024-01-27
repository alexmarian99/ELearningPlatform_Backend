package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;

    public List<Week> findAllWeeksByModuleId(int moduleId){
        return weekRepository.findAllByModuleId(moduleId);
    }
    public Week saveWeek(Week week){
        return weekRepository.save(week);
    }
}
