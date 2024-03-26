    package cleancode.eLearningPlatform.specialKatas.controller;

    import cleancode.eLearningPlatform.auth.model.Response;
    import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
    import cleancode.eLearningPlatform.specialKatas.enums.Category;
    import cleancode.eLearningPlatform.specialKatas.model.Kata;
    import cleancode.eLearningPlatform.specialKatas.model.KataPaginationResponse;
    import cleancode.eLearningPlatform.specialKatas.model.PaginationRequest;
    import cleancode.eLearningPlatform.specialKatas.service.KataService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
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

        @PatchMapping("/addUserToKata")
        public ResponseEntity<Response> addOrRemoveUserFromKata(@RequestParam (name = "userId") Long userId, @RequestParam (name = "kataId") Integer kataId ){
            return ResponseEntity.ok(kataService.addOrRemoveUserFromKata(userId,kataId));
        }

        @DeleteMapping("/{kataId}")
        public ResponseEntity<Response> deleteKata(@PathVariable int kataId) {
            return  ResponseEntity.ok(kataService.deleteKata(kataId));
        }

        @GetMapping("/filtered")
        public ResponseEntity<KataPaginationResponse> getFilteredKatas(
                @RequestParam(name= "category") Category category,
                @RequestParam(name = "status") String status,
                @RequestParam(name = "level") String level,
                @RequestParam(name = "userId") Long userId,
                @RequestParam(name = "pageNumber") Integer pageNumber,
                @RequestParam(name = "numberOfItems") Integer numberOfItems){

            Integer convertedlevel = level.equals("ALL") ? null : Integer.parseInt(level);
            category = category.equals(Category.ALL) ? null : category;
            status = status.equals("ALL") ? null : status;
            Pageable pageable = PageRequest.of(pageNumber, numberOfItems);

            // Call the service method to retrieve filtered katas based on the provided criteria
            KataPaginationResponse filteredKatas = kataService.getFilteredKatas(category, status, convertedlevel, userId, pageable);

            // Check if any katas were found based on the filter criteria
            if (filteredKatas.getNumberOfKatas() == 0) {
                return ResponseEntity.noContent().build(); // Return 204 No Content if no katas match the criteria
            } else {
                return ResponseEntity.ok(filteredKatas); // Return the filtered katas with status 200 OK
            }
        }




    }
