package cleancode.eLearningPlatform.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder

public class CompletedStuff {
    private List<Integer> completedLessons = new ArrayList<>();
    private List<Integer> completedWeeks = new ArrayList<>();

    public CompletedStuff(List<Integer> completedLessons, List<Integer> completedWeeks) {
        this.completedLessons = completedLessons;
        this.completedWeeks = completedWeeks;
    }
}
