package cleancode.eLearningPlatform.auth.service;



import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.config.JWTService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
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

        if(usernameExists){
            return AuthenticationResponse.builder().response("0").build();
        }

        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .password( passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().response(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
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


    public Response addOrRemoveLessonFromUser(Long userId, Integer lessonId, Integer weekId, Status status) {
        Optional<User> optionalUser = userRepository.findById(userId);
        List<Lesson> lessons = lessonRepository.getRestOfLessons(lessonId);
        // System.out.println(status + " " + userId + " " + lessonId + " weekID " + weekId);

        long result = lessons.stream().filter(lesson -> optionalUser.get().getCompletedLessons().contains(lesson.getId())).count() + 1;
        System.out.println("Lessons " + lessons.size() + " completed " + (result));


        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            if(status.equals(Status.DONE)){
                user.getCompletedLessons().add(lessonId);
                if(result == lessons.size()){
                    System.out.println("ADD WEEK");
                    user.getCompletedWeeks().add(weekId);
                }
            }else{
               user.getCompletedLessons().remove(Integer.valueOf(lessonId));
                if(result - lessons.size() == 1){
                    System.out.println("REMOVE WEEEKKKKKKKKKKKKKKKKKKKKKK");
                    user.getCompletedWeeks().remove(Integer.valueOf(weekId));
                }
            }
            userRepository.save(user);
        }


        return Response.builder().response("ok").build();
    }

}
