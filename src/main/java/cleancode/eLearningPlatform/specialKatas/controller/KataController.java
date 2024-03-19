    package cleancode.eLearningPlatform.specialKatas.controller;

    import cleancode.eLearningPlatform.auth.model.Response;
    import cleancode.eLearningPlatform.specialKatas.model.Kata;
    import cleancode.eLearningPlatform.specialKatas.model.KataPaginationResponse;
    import cleancode.eLearningPlatform.specialKatas.model.PaginationRequest;
    import cleancode.eLearningPlatform.specialKatas.service.KataService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/katas")
    public class KataController {

        private final KataService kataService;



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
        @PostMapping("/getKatas")
        public KataPaginationResponse getAllKatas(@RequestBody PaginationRequest paginationResponse) {
            return kataService.findAllKatas(paginationResponse);
        }

        @DeleteMapping("/{kataId}")
        public ResponseEntity<Response> deleteKata(@PathVariable int kataId) {

            return  ResponseEntity.ok(kataService.deleteKata(kataId));
        }
    }
