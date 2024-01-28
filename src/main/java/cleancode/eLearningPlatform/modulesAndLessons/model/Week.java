package cleancode.eLearningPlatform.modulesAndLessons.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Week {


    @Id
    @GeneratedValue
    private int id;

    private int number;
    private String name;

    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)
    private Module module;

    @JsonManagedReference
    @OneToMany(mappedBy = "week",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

}
