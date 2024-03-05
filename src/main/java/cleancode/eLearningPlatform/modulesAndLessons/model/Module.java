package cleancode.eLearningPlatform.modulesAndLessons.model;


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

    @Column(columnDefinition = "TEXT")
    private String imgLink;

    @JsonBackReference
    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Week> weeks = new ArrayList<>();


    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", imgLink='" + imgLink + '\'' +
                ", weeks=" + weeks +
                '}';
    }
}
