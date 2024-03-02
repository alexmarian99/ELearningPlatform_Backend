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
        return userRepository.findByUsername(jwtService.extractUsername(token)).orElse(null);
    }


    public Response addOrRemoveLessonFromUser(Long userId, Integer lessonId, Integer weekId, Status status) {
        User user = userRepository.findById(userId).orElse(null);
        List<Lesson> lessons = lessonRepository.getRestOfLessons(lessonId);
        long completedLessonsBefore = lessons.stream().filter(lesson -> {
            assert user != null;
            return user.getCompletedLessons().contains(lesson.getId());
        }).count();

            if (status.equals(Status.DONE)) {
                assert user != null;
                user.getCompletedLessons().add(lessonId);
                if (completedLessonsBefore + 1 == lessons.size()) {
                    System.out.println("ADD WEEK FROM STATUS -> " + weekId);
                    user.getCompletedWeeks().add(weekId);
                    checkAndModifyModuleStatus(user, weekId, status);
                }
            } else {
                assert user != null;
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

    private void checkAndModifyModuleStatus(User user, int weekId, Status status){
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


    @SafeVarargs
    public final void removeModuleFromAllUsers(Module module, boolean cascadeDelete, List<User>... optionalUsers){
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        if(cascadeDelete){
            users.forEach(user -> {
                module.getWeeks().forEach(week -> removeWeekFromAllUsers(week, true, false ,  users));
                user.getCompletedModules().remove(Integer.valueOf(module.getId()));
            });
        }else{
            users.forEach(user -> {
                user.getCompletedModules().remove(Integer.valueOf(module.getId()));
            });
        }

    }

    @SafeVarargs
    public final void removeWeekFromAllUsers(Week week, boolean cascadeDelete, boolean removeJustAllWeeksStatus, List<User>... optionalUsers){
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        if(removeJustAllWeeksStatus){
            // HERE WE ARE REMOVING JUST THE WEEK STATUS, THIS HAPPENS WHEN A NEW LESSON ITS ADDED AND THE WEEK RETURNS TO TODO
            System.out.println("// HERE WE ARE REMOVING JUST THE WEEK STATUS, THIS HAPPENS WHEN A NEW LESSON ITS ADDED AND THE WEEK RETURNS TO TODO");
            users.forEach(user -> {
                user.getCompletedWeeks().remove(Integer.valueOf(week.getId()));
            });
        }else{
            // HERE WE ARE DELETING THE WHOLE WEEK WITH ALL HER RECORDS
            System.out.println(" // HERE WE ARE DELETING THE WHOLE WEEK WITH ALL HER RECORDS");
            List<Lesson> lessonsOfWeek = weekRepository.getLessonsByWeekId(week.getId());
            if(cascadeDelete){
                // THIS MEANS THAT A MODULE IS DELETED AND ALL THE WEEKS WILL BE DELETED SO WE WON'T NEED TO CHECK FURTHER STATUS
                users.forEach(user -> {
                    lessonsOfWeek.forEach(lesson -> removeLessonFromAllUsers(lesson.getId(), week.getId(),true , users));
                    user.getCompletedWeeks().remove(Integer.valueOf(week.getId()));

                });
            }else{
                // THIS MEANS THAT JUST ONE WEEK IS DELETED AND  WE NEED TO CHECK FURTHER STATUS FOR THE MODULE
                users.forEach(user -> {
                    lessonsOfWeek.forEach(lesson -> removeLessonFromAllUsers(lesson.getId(), week.getId(),true , users));
                    user.getCompletedWeeks().remove(Integer.valueOf(week.getId()));
                     modifyModuleStatusAfterWeekDeleteOrAdd(week.getId(), user, Status.TODO);

                });
            }
        }

    }

    @SafeVarargs
    public final void removeLessonFromAllUsers(Integer lessonId, Integer weekId, boolean cascadeDelete, List<User>... optionalUsers){
        List<User> users = optionalUsers.length > 0 ? optionalUsers[0] : userRepository.findAll();

        if(cascadeDelete){
            System.out.println("DELETING ALL LESSONS FROM USERS");
            users.forEach(user -> user.getCompletedLessons().remove(Integer.valueOf(lessonId)));
        } else {
            System.out.println("DELETE ONE AND CHECK THE STATUS FURTHER");
            for (User user : users) {
                if (user.getCompletedLessons().contains(lessonId)) {
                    user.getCompletedLessons().remove(Integer.valueOf(lessonId));

                    System.out.println("Lesson removed from user: " + user.getUsername());
                } else {
                    System.out.println("Lesson not found for user: " + user.getUsername());
//                    modifyWeekStatusAfterLessonDelete(lessonId, weekId, user);
                }
            }
        }
    }

    private void modifyWeekStatusAfterLessonDelete(Integer lessonId, Integer weekId, User user){
        List<Lesson> lessonsOfWeek = lessonRepository.getRestOfLessons(lessonId);
        List<Integer> completedLessons = user.getCompletedLessons();
        long completedLessonsAfter = lessonsOfWeek.stream().filter(lesson -> completedLessons.contains(lesson.getId())).count();

        if(lessonsOfWeek.size() - completedLessonsAfter == 1){
            user.getCompletedWeeks().add(weekId);
           // modifyModuleStatusAfterWeekDeleteOrAdd(weekId, user, Status.DONE);
        }
    }


    public void modifyModuleStatusAfterWeekDeleteOrAdd( Integer weekId, User user ,Status addOrRemoveWeek){
       List<Week> weeksOfModule = weekRepository.getRestOfWeeks(weekId);
        List<Integer> completedWeeks = user.getCompletedWeeks();
        long completedWeeksAfter = weeksOfModule.stream().filter(week -> completedWeeks.contains(week.getId())).count();

        System.out.println("WEEKS OF MODULES SIZE -> " + weeksOfModule.size());
        System.out.println("COMPLETED WEEKS -> " + completedWeeksAfter ) ;
        System.out.println("ARE EQAL -> " + (weeksOfModule.size() == completedWeeksAfter));

        if(addOrRemoveWeek.equals(Status.TODO) && weeksOfModule.size() - completedWeeksAfter == 1){
            System.out.println("ADD MODULE FROM REMOVE WEEK");
            user.getCompletedModules().add(weekRepository.getModuleIdFromWeek(weekId));
        }else if(addOrRemoveWeek.equals(Status.DONE) && weeksOfModule.size() == completedWeeksAfter){
            System.out.println("ADD MODULE FROM ADD WEEK");
            user.getCompletedModules().add(weekRepository.getModuleIdFromWeek(weekId));
        }

//        if((addOrRemoveWeek.equals(Status.TODO) && weeksOfModule.size() - completedWeeksAfter == 1)
//                || (addOrRemoveWeek.equals(Status.DONE) && weeksOfModule.size() == completedWeeksAfter)){
//            user.getCompletedModules().add(weekRepository.getModuleIdFromWeek(weekId));
//            userRepository.save(user);
//        }
    }

    public CompletedStuff getCompletedStuff(Integer userId) {
        List<Integer> lessons = userRepository.getJustLessons(userId);
        List<Integer> weeks = userRepository.getJustWeeks(userId);
        List<Integer> modules = userRepository.getJustModules(userId);

        return CompletedStuff.builder().completedLessons(lessons).completedWeeks(weeks).completedModules(modules).build();
    }
}
