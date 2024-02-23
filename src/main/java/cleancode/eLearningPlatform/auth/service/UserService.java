package cleancode.eLearningPlatform.auth.service;


import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.config.JWTService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
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



        System.out.println("REST OF LESSONS " + lessons);
        System.out.println(" function addOrRemoveLessonFromUser callde ----------------------------------------------");
//        System.out.println(user.toString() + " optional user -----------------------------------------------");
//        System.out.println(userRepository.findById(userId).get().toString() + " optional user -----------------------------------------------");

        if (status.equals(Status.DONE)) {
            user.getCompletedLessons().add(lessonId);
        } else {
            user.getCompletedLessons().remove(Integer.valueOf(lessonId));
        }

        long result = lessons.stream().filter(lesson -> user.getCompletedLessons().contains(lesson.getId())).count();
        System.out.println("RESULT AND LESSONS SIZE " + result + "  " + lessons.size());

        if (result == lessons.size()) {
            System.out.println("ADD WEEK");
            user.getCompletedWeeks().add(weekId);
        }

        if (lessons.size() - result  == 1) {
            System.out.println("REMOVE WEEEKKKKKKKKKKKKKKKKKKKKKK");
            user.getCompletedWeeks().remove(Integer.valueOf(weekId));
        }

        userRepository.save(user);
        return Response.builder().response("ok").build();
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

        //first try

//        System.out.println(lessons);
//        System.out.println(weeks);
//
//        List<List<Integer>> lessonsAndWeeks = userRepository.getLessonsAndWeeks(userId);
//        System.out.println(lessonsAndWeeks);
//
//        List<List<Integer>> test3 = userRepository.test3(userId);
//        System.out.println(test3);

        //second try

//        List<Object[]> resultList = userRepository.getLessonsAndWeeks(userId);
//        List<List<Integer>> combinedList = new ArrayList<>();
//        for (Object[] result : resultList) {
//            System.out.println(result[0]);
//            System.out.println(result[1]);
//
//            System.out.println();
//            List<Integer> lessons1 = (List<Integer>) result[0];
//            List<Integer> weeks1 = (List<Integer>) result[1];
//
//            System.out.println(lessons1);
//            System.out.println(weeks1);
//
//            List<Integer> combined = new ArrayList<>();
//            combined.addAll(lessons1);
//            combined.addAll(weeks1);
//
//            System.out.println(combined);
//
//            combinedList.add(combined);
        //       }

        return CompletedStuff.builder().completedLessons(lessons).completedWeeks(weeks).build();
    }
}
