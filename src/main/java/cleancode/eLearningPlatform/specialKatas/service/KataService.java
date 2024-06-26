package cleancode.eLearningPlatform.specialKatas.service;

import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.specialKatas.model.Kata;
import cleancode.eLearningPlatform.specialKatas.model.KataPaginationResponse;
import cleancode.eLearningPlatform.specialKatas.repository.KataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class KataService {
    private final KataRepository kataRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    Random random = new Random();
    private List<Kata> kataOfTheDay = new ArrayList<>();

    @Cacheable(value = "kataCache", key = "#root.methodName")
    public List<Kata> getKataOfTheDay() {
        return kataOfTheDay;
    }

    //    cron = "0 0 0 * * ?"  runs daily at midnight
//    fixedRate = 1000 * 20 runs at a timer
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void selectKataOfTheDay() {
        List<Kata> firstGradeKatas = kataRepository.findAllByLevel(List.of(1, 2));
        List<Kata> secondGradeKatas = kataRepository.findAllByLevel(List.of(3, 4));
        List<Kata> thirdGradeKatas = kataRepository.findAllByLevel(List.of(5, 6));
        List<Kata> fourthGradeKatas = kataRepository.findAllByLevel(List.of(7, 8));

        kataOfTheDay = getOneRandomItemFromLists(List.of(firstGradeKatas, secondGradeKatas, thirdGradeKatas, fourthGradeKatas));
    }

    public Kata saveKata(Kata kata, String authHeader) {
        if(!userService.checkIfUserAdmin(authHeader)) return null;
        Optional<Kata> existingKata = kataRepository.findByTitleAndKataLink(kata.getTitle(), kata.getKataLink());
        // Kata does not exist, save it
        return existingKata.orElseGet(() -> kataRepository.save(kata));
    }
    public void saveKata(List<Kata> katas, String authHeader) {
        if(!userService.checkIfUserAdmin(authHeader)) return;
        kataRepository.deleteAll();
        kataRepository.saveAll(katas);
    }

    public boolean kataExists(String title, String kataLink ) {
        Optional<Kata> existingKata = kataRepository.findByTitleAndKataLink(title, kataLink);
        return existingKata.isPresent();
    }

    @Modifying
    @Transactional
    public Response deleteKata(int id, String authHeader) {
        if(!userService.checkIfUserAdmin(authHeader)) return null;
        kataRepository.deleteById(id);
        return Response.builder().response("Kata deleted").build();
    }

    private List<Kata> getOneRandomItemFromLists(List<List<Kata>> lists) {
        List<Kata> result = new ArrayList<>();
        for (List<Kata> kataList : lists) {
            int randomIndex = random.nextInt(kataList.size());
            result.add(kataList.get(randomIndex));
        }

        return result;
    }

    public Kata getKataById(int kataId) {
        return kataRepository.findById(kataId).orElse(null);
    }

    public Kata editKata(Kata kata, String authHeader) {
        if(!userService.checkIfUserAdmin(authHeader)) return null;
        Optional<Kata> kataFromDB = kataRepository.findByTitle(kata.getTitle());

        if (kataFromDB.isPresent() && kataFromDB.get().getId() != kata.getId() ) {
            return null;
        } else {
            return kataRepository.save(kata);
        }
    }

    public KataPaginationResponse getFilteredKatas(String category, String status, Integer level, Long userId, String searchByName, Pageable pageable) {
        System.out.println(searchByName);
        List<Kata> initialFilter = kataRepository.findByFilters(level, category, userId, status, searchByName,  pageable);
        long totalNumberOfKatas = kataRepository.countFindByFilters(level, category, userId, status, searchByName );

        return KataPaginationResponse
                .builder()
                .katas(initialFilter)
                .numberOfKatas(totalNumberOfKatas)
                .build();
    }

    public Response addOrRemoveUserFromKata(Long userId, int kataId, String authHeader) {
        System.out.println("Here we are in service function");
        System.out.println(userId + " " + kataId + " " + authHeader);

        //if(!userService.checkIfUserAdmin(authHeader)) return null;

        User user = userRepository.findById(userId).orElse(null);
        Kata kata = kataRepository.findById(kataId).orElse(null);

        assert user != null;
        assert kata != null;

        if (!kata.getCompletedByUsers().contains(userId)) {
            if (user.getRankPoints() == null) {
                user.setRankPoints(0);
            }
            kata.getCompletedByUsers().add(userId);
            user.setRankPoints(user.getRankPoints() + (54 - (kata.getLevel() * 6)));
            user.setWeeklyRankPoints(user.getWeeklyRankPoints() + (54 - (kata.getLevel() * 6)));
        }
        kataRepository.save(kata);
        userRepository.save(user);
        return Response.builder().response("User has been updated").build();
    }

}
