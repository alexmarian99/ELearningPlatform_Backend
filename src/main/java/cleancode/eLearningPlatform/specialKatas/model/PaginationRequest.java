package cleancode.eLearningPlatform.specialKatas.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {
    private int page;
    private int numberOfItems;
    private String filter;
    private String filterValue;
}