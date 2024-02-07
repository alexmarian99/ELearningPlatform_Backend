package cleancode.eLearningPlatform.specialKatas.model;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.specialKatas.enums.Category;
import cleancode.eLearningPlatform.specialKatas.enums.Language;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Kata {
    @Id
    @GeneratedValue
    private int id;

    private String title;
    private String description;

    @Min(1)
    @Max(10)
    private int level;

    private Language language;
    private Category category;

    @JsonBackReference
    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
