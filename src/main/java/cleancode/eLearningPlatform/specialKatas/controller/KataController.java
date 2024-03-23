    package cleancode.eLearningPlatform.specialKatas.controller;

    import cleancode.eLearningPlatform.auth.model.Response;
    import cleancode.eLearningPlatform.specialKatas.enums.Category;
    import cleancode.eLearningPlatform.specialKatas.model.Kata;
    import cleancode.eLearningPlatform.specialKatas.model.KataPaginationResponse;
    import cleancode.eLearningPlatform.specialKatas.model.PaginationRequest;
    import cleancode.eLearningPlatform.specialKatas.service.KataService;
    import lombok.RequiredArgsConstructor;
    import org.hibernate.sql.results.graph.entity.internal.AbstractNonJoinedEntityFetch;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Repository;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/katas")
    public class KataController {
        private final KataService kataService;

        @GetMapping("/katasOfTheDay")
        public ResponseEntity<List<Kata>> getKatasOfTheDay(@RequestParam boolean requestRefresh){
            if(requestRefresh){
                kataService.selectKataOfTheDay();
            }

            return ResponseEntity.ok(kataService.getKataOfTheDay());
        }

        @GetMapping("/{kataId}")
        public ResponseEntity<Object> getKataById(@PathVariable int kataId){
            Kata kata = kataService.getKataById(kataId);
            if(kata != null){
                return ResponseEntity.ok(kataService.getKataById(kataId));
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Kata does not exists");
            }
        }

        @PostMapping("/getKatas")
        public KataPaginationResponse getAllKatas(@RequestBody PaginationRequest paginationResponse) {
            return kataService.findAllKatas(paginationResponse);
        }

        @PostMapping
        public ResponseEntity<Object> saveOneKata(@RequestBody Kata kata) {
            // Check if a kata with the same title and link already exists
            boolean kataExists = kataService.kataExists(kata.getTitle(), kata.getKataLink());
            if (kataExists) {
                // Kata with the same title and link already exists, return a conflict response
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Kata already exists");
            } else {
                // Kata does not exist, save it and return a success response
                Kata savedKata = kataService.saveKata(kata);
                return ResponseEntity.ok(savedKata);
            }
        }

        @PutMapping("/{kataId}")
        public ResponseEntity<Object> editKata(@RequestBody Kata kata){
            Kata dbKata = kataService.editKata(kata);
            if(dbKata != null){
                return ResponseEntity.ok(dbKata);
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This kata already exists");
            }
        }

        @DeleteMapping("/{kataId}")
        public ResponseEntity<Response> deleteKata(@PathVariable int kataId) {

            return  ResponseEntity.ok(kataService.deleteKata(kataId));
        }
        @GetMapping("/filtered")
        public ResponseEntity<KataPaginationResponse> getFilteredKatas(
                @RequestParam(name= "category") Category category,
                @RequestParam(name = "status") String status,
                @RequestParam(name = "difficulty") String difficulty,
                @RequestParam (name = "userId") Long userId,
                @RequestParam (name = "pageNumber") Integer pageNumber,
                @RequestParam (name = "numberOfItems") Integer numberOfItems){

            Integer convertedDifficulty = difficulty.equals("ALL") ? 0 : Integer.parseInt(difficulty);
            Pageable pageable = PageRequest.of(pageNumber, numberOfItems);

            System.out.println(category);
            System.out.println(status);
            System.out.println(convertedDifficulty);
            System.out.println(userId);

            System.out.println("page nr " + pageNumber);
            System.out.println("nr items " + numberOfItems);

            // Call the service method to retrieve filtered katas based on the provided criteria
            KataPaginationResponse filteredKatas = kataService.getFilteredKatas(category, status, convertedDifficulty, userId, pageable);

            // Check if any katas were found based on the filter criteria
            if (filteredKatas.getNumberOfKatas() == 0) {
                return ResponseEntity.noContent().build(); // Return 204 No Content if no katas match the criteria
            } else {
                return ResponseEntity.ok(filteredKatas); // Return the filtered katas with status 200 OK
            }
        }




    }
