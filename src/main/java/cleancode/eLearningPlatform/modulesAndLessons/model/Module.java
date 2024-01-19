package cleancode.eLearningPlatform.modulesAndLessons.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Module {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Lesson> lessons;

}