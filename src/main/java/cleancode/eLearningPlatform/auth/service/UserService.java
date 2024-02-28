package cleancode.eLearningPlatform.auth.service;


import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.config.JWTService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final WeekRepository weekRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        boolean usernameExists = userRepository.existsByUsername(registerRequest.getUsername());

        if (usernameExists) {
            return AuthenticationResponse.builder().response("0").build();
        }

        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().response(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ));
        var user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().response(jwtToken).build();
    }

    public User getUserWithToken(String authHeader) {
        String token = authHeader.substring(7);
        User user = userRepository.findByUsername(jwtService.extractUsername(token)).orElse(null);
        return user;
    }

    public Response addOrRemoveStatusOnDelete(Integer lessonId, User user){
        user.getCompletedLessons().remove(Integer.valueOf(lessonId));
        List<Lesson> lessons = lessonRepository.getRestOfLessons(lessonId);
        long completedLessonsAfter = lessons.stream().filter(lesson -> user.getCompletedLessons().contains(lesson.getId())).count();

        System.out.println("STATUS MODIFIED ---------------------");
        return null;

//        if(lessons.size() - completedLessonsBefore == 1 && completedLessonsAfter == lessons.size() -1){
//            System.out.println("ADD WEEK FROM DELETE -> " + weekId);
//            user.getCompletedWeeks().add(weekId);
//            checkAndModifyModuleStatus(user, weekId, status);
//        }
//        if(completedLessonsAfter == 0 && completedLessonsBefore == 0){
//            System.out.println("REMOVE WEEK FROM DELETE -> " + weekId);
//            user.getCompletedWeeks().remove(Integer.valueOf(weekId));
//            checkAndModifyModuleStatus(user, weekId, status);
//        }
    }


    public Response addOrRemoveLessonFromUser(Long userId, Integer lessonId, Integer weekId, Status status) {
        User user = userRepository.findById(userId).orElse(null);
        List<Lesson> lessons = lessonRepository.getRestOfLessons(lessonId);
        long completedLessonsBefore = lessons.stream().filter(lesson -> user.getCompletedLessons().contains(lesson.getId())).count();

            if (status.equals(Status.DONE)) {
                user.getCompletedLessons().add(lessonId);
                if (completedLessonsBefore + 1 == lessons.size()) {
                    System.out.println("ADD WEEK FROM STATUS -> " + weekId);
                    user.getCompletedWeeks().add(weekId);
                    checkAndModifyModuleStatus(user, weekId, status);
                }
            } else {
                user.getCompletedLessons().remove(Integer.valueOf(lessonId));
                if (lessons.size() - completedLessonsBefore == 0) {
                    System.out.println("REMOVE WEEK FROM STATUS -> " + weekId);
                    user.getCompletedWeeks().remove(Integer.valueOf(weekId));
                    checkAndModifyModuleStatus(user, weekId, status);
                }
            }

        userRepository.save(user);
        return Response.builder().response("ok").build();
    }

    public void checkAndModifyModuleStatus(User user, int weekId, Status status){
        List<Week> restOfWeeks = weekRepository.getRestOfWeeks(weekId);
        long completedWeeksBefore = restOfWeeks.stream().filter(week -> user.getCompletedWeeks().contains( week.getId())).count();
        int moduleId = weekRepository.getModuleIdFromWeek(weekId);

        if(completedWeeksBefore == restOfWeeks.size()){
            System.out.println("ADD MODULE ---------------------------------------");
            user.getCompletedModules().add(moduleId);
        }
        if(restOfWeeks.size() - completedWeeksBefore == 1 && status.equals(Status.TODO)){
            System.out.println("REMOVE MODULE ----------------------------");
            user.getCompletedModules().remove(Integer.valueOf(moduleId));
        }
    }


    //to replace and delete
    public void removeOrAddWeekFromAllUser(Integer weekId) {
        List<User> users = userRepository.findAll();
        users.stream().forEach(user -> {
            user.getCompletedWeeks().remove(Integer.valueOf(weekId));
            userRepository.save(user);
        });
    }

    public void removeModuleFromAllUsers(Module module, List<User>... optionalUsers){
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        users.stream().forEach( user -> {
            module.getWeeks().stream().forEach(week -> removeWeekFromAllUsers(week, users));
            user.getCompletedModules().remove(Integer.valueOf(module.getId()));
        });
    }

    public void removeWeekFromAllUsers(Week week, List<User>... optionalUsers){
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        users.stream().forEach( user -> {
            week.getLessons().stream().forEach(lesson -> removeLessonFromAllUsers(lesson.getId(), users));
            user.getCompletedWeeks().remove(Integer.valueOf(week.getId()));
        });
    }

    public void removeLessonFromAllUsers(Integer lessonId, List<User>... optionalUsers){
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        users.stream().forEach( user -> user.getCompletedLessons().remove(Integer.valueOf(lessonId)));
    }

    public CompletedStuff getCompletedStuff(Integer userId) {
        List<Integer> lessons = userRepository.getJustLessons(userId);
        List<Integer> weeks = userRepository.getJustWeeks(userId);
        List<Integer> modules = userRepository.getJustModules(userId);

        return CompletedStuff.builder().completedLessons(lessons).completedWeeks(weeks).completedModules(modules).build();
    }
}
