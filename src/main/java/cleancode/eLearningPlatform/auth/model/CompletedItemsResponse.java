package cleancode.eLearningPlatform.auth.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder

public class CompletedItemsResponse {
    private List<Integer> completedLessons = new ArrayList<>();
    private List<Integer> completedWeeks = new ArrayList<>();
    private List<Integer> completedModules = new ArrayList<>();


}
