package cleancode.eLearningPlatform.courses.model;

import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private int id;
    private String courseName;
    private String courseCode;

    //so if you dont attribute mapped by in the oneToMany you dont need to put the ref other way
    @JsonBackReference
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Module> modules = new ArrayList<>();



    // if more courses add Course class who has modules,
    // so we add in user courseName and courseCode

}
