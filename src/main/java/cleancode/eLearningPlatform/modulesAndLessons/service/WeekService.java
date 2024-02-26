package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;
    private final LessonService lessonService;
    private final UserRepository userRepository;

    public Week findWeekById(int weekId){
        return weekRepository.findById(weekId).orElse(null);
    }

    public List<Week> findAllWeeksByModuleId(int moduleId){
        return weekRepository.findAllByModuleIdOrderByNumber(moduleId);
    }
    public Week saveWeek(Week week){
        return weekRepository.save(week);
    }

    @Transactional
    @Modifying
    public String deleteWeekById(Integer weekId, List<User>... optionalUsers){
        Week deletedWeek = weekRepository.findById(weekId).orElse(null);
        List<User> users = optionalUsers.length == 0 ? userRepository.findAll() : optionalUsers[0];
        System.out.println(users + "_____________________________________");

        deletedWeek.getLessons().stream().forEach(lesson -> lessonService.deleteLesson(lesson.getId(), weekId, users));

        weekRepository.delete(deletedWeek);
        return "Deleted Week " +weekId+ " Succesfull";
    }

    public Week updateWeek(int weekId, Week updatedWeek){
        Optional<Week> existingWeekOptional = weekRepository.findById(weekId);

        if(existingWeekOptional.isPresent()){
            Week existingWeek = existingWeekOptional.get();

            existingWeek.setName(updatedWeek.getName());
            existingWeek.setNumber(updatedWeek.getNumber());
            existingWeek.setImgLink(updatedWeek.getImgLink());

            return weekRepository.save(existingWeek);
        }else{
            throw new IllegalArgumentException("Week with id: " + weekId + "can't be found");
        }
    }
}
