package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findLessonByWeekId(int weekId){
        return lessonRepository.findAllByWeekIdOrderByWeek(weekId);
    }

    public Lesson findLessonById(int lessonId){
        return lessonRepository.findById(lessonId).orElse(null);
    }

   public Lesson saveLesson( Lesson lesson){
        userService.removeOrAddWeekFromAllUser(lesson.getWeek().getId());
        return lessonRepository.save(lesson);
    }

    public String deleteLesson(Integer lessonId, Integer weekId) {
        List<User> users = userRepository.findAll();
        System.out.println("in delete ------------------------------------------------------------");

        for (User user : users ) {

//            user.getCompletedLessons().removeIf(item -> item == lessonId);
            userService.addOrRemoveLessonFromUser(user.getId(), lessonId, weekId, Status.TODO, user);
//            userRepository.save(user);
        }

        System.out.println( " before delete lesson----------------------------------------------");
        lessonRepository.deleteById(lessonId);
        System.out.println(" after delete lesson ----------------------------------------------");
        return "Deleted Lesson " + lessonId;
    }


    public Lesson updateLesson(int lessonId, Lesson updatedLesson){
        Optional<Lesson> existUpdatedOptional = lessonRepository.findById(lessonId);

        if(existUpdatedOptional.isPresent()){
            Lesson existingLesson = existUpdatedOptional.get();

            existingLesson.setName(updatedLesson.getName());
            existingLesson.setDescription(updatedLesson.getDescription());
            existingLesson.setGitHubLink(updatedLesson.getGitHubLink());

            return lessonRepository.save(existingLesson);
        }else{
            throw new IllegalArgumentException("Lesson can't be found with id : "+ lessonId);
        }
    }
}
