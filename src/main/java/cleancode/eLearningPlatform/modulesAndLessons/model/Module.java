package cleancode.eLearningPlatform.modulesAndLessons.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private int number;

    @Column(length = 1000)
    private String imgLink;

    @JsonManagedReference
    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Week> weeks = new ArrayList<>();


}
