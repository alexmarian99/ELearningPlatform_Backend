package cleancode.eLearningPlatform.modulesAndLessons.model;


import cleancode.eLearningPlatform.courses.model.Course;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

//    @JsonBackReference("weeks")
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Week> weeks = new ArrayList<>();

    @JsonBackReference("course")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;


    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", weeks=" + weeks +
                '}';
    }
}
