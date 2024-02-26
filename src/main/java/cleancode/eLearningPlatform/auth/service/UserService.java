package cleancode.eLearningPlatform.auth.service;


import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.config.JWTService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import cleancode.eLearningPlatform.modulesAndLessons.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Response addOrRemoveLessonFromUser(Long userId, Integer lessonId, Integer weekId, Status status, User... optionalUser) {
        User user = optionalUser.length != 0 ? optionalUser[0] : userRepository.findById(userId).orElse(null);
        List<Lesson> lessons = lessonRepository.getRestOfLessons(lessonId);
        long completedLessonsBefore = lessons.stream().filter(lesson -> user.getCompletedLessons().contains(lesson.getId())).count();

        // if the optional user is present (optionalUser.length != 0),that means that its a delete operation going so the status needs to change differently
        if(optionalUser.length == 0) {
            if (status.equals(Status.DONE)) {
                user.getCompletedLessons().add(lessonId);
                if (completedLessonsBefore + 1 == lessons.size()) {
                    user.getCompletedWeeks().add(weekId);
                    checkAndModifyModuleStatus(user, weekId, status);
                }
            } else {
                user.getCompletedLessons().remove(Integer.valueOf(lessonId));
                if (lessons.size() - completedLessonsBefore == 0) {
                    user.getCompletedWeeks().remove(Integer.valueOf(weekId));
                    checkAndModifyModuleStatus(user, weekId, status);
                }
            }
        }else{
            user.getCompletedLessons().remove(Integer.valueOf(lessonId));
            long completedLessonsAfter = lessons.stream().filter(lesson -> user.getCompletedLessons().contains(lesson.getId())).count();
            if(lessons.size() - completedLessonsBefore == 1 && completedLessonsAfter == lessons.size() -1){
                user.getCompletedWeeks().add(weekId);
                checkAndModifyModuleStatus(user, weekId, status);
            }
           if(completedLessonsAfter == 0){
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
            System.out.println("DELTE MODULE ----------------------------");
            user.getCompletedModules().remove(Integer.valueOf(moduleId));
        }
    }

    public void removeOrAddWeekFromAllUser(Integer weekId) {
        List<User> users = userRepository.findAll();
        users.stream().forEach(user -> {
            user.getCompletedWeeks().remove(Integer.valueOf(weekId));
            userRepository.save(user);
        });
    }

    public CompletedStuff getCompletedStuff(Integer userId) {
        List<Integer> lessons = userRepository.getJustLessons(userId);
        List<Integer> weeks = userRepository.getJustWeeks(userId);
        List<Integer> modules = userRepository.getJustModules(userId);

        return CompletedStuff.builder().completedLessons(lessons).completedWeeks(weeks).completedModules(modules).build();
    }
}
