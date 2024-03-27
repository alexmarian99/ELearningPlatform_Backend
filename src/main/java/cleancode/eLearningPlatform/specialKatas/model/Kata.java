package cleancode.eLearningPlatform.specialKatas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Kata {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String kataLink;

    @Min(1)
    @Max(8)
    private int level;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "kata_categories", joinColumns = @JoinColumn(name = "kata_id"))
    @Enumerated(EnumType.STRING)
    private List<String> category;

    @ElementCollection( fetch = FetchType.EAGER)
    @CollectionTable(name = "completed_kata2", joinColumns = @JoinColumn(name = "kata_id"))
    private List<Long> completedByUsers = new ArrayList<>();
}
