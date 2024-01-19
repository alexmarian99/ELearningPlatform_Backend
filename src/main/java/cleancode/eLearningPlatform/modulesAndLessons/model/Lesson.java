package cleancode.eLearningPlatform.modulesAndLessons.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @Column(length = 1000)
    private String description;

    private String gitHubLink;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Module module;

}
