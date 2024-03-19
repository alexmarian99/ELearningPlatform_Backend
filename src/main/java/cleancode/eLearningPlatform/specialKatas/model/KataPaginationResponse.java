package cleancode.eLearningPlatform.specialKatas.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KataPaginationResponse {
    private long numberOfKatas;
    private List<Kata> katas;
}
