package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final WeekRepository weekRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findLessonByWeekId(int weekId){
        return lessonRepository.findAllByWeekIdOrderByOptional(weekId);
    }


    public Lesson findLessonById(int lessonId){
        return lessonRepository.findById(lessonId).orElse(null);
    }

    @Transactional
    @Modifying
   public Lesson saveLesson( Lesson lesson){
        Module module = moduleRepository.findById(lesson.getWeek().getModule().getId()).orElse(null);
        Week week = weekRepository.findById(lesson.getWeek().getId()).orElse(null);

        userService.removeWeekFromAllUsers(week , false, true, new ArrayList<>() );
        userService.removeModuleFromAllUsers(module, false, new ArrayList<>());

        return lessonRepository.save(lesson);
    }


    @Transactional
    @Modifying
    public String deleteLesson(Integer lessonId, Integer weekId, List<User>... optionalUsers) {
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        lessonRepository.deleteById(lessonId);
        userService.removeLessonFromAllUsers(lessonId, weekId , false ,users);

        System.out.println("DELETE LESSON -> " + lessonId + " ---------------------------------------------");
        return "Deleted Lesson " + lessonId;
    }


    public Lesson updateLesson(int lessonId, Lesson updatedLesson){
        Optional<Lesson> existUpdatedOptional = lessonRepository.findById(lessonId);

        if(existUpdatedOptional.isPresent()){
            Lesson existingLesson = existUpdatedOptional.get();

            existingLesson.setName(updatedLesson.getName());
            existingLesson.setDescription(updatedLesson.getDescription());
            existingLesson.setGitHubLink(updatedLesson.getGitHubLink());
            existingLesson.setOptional(updatedLesson.isOptional());

            return lessonRepository.save(existingLesson);
        }else{
            throw new IllegalArgumentException("Lesson can't be found with id : "+ lessonId);
        }
    }
}
