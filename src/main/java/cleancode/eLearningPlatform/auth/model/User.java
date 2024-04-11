package cleancode.eLearningPlatform.auth.model;

import cleancode.eLearningPlatform.specialKatas.model.Kata;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name ="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private short phoneNumber;

    private String location;
    private String address;

    private String githubUsername;
    private String codeWarsUsername;
    private Integer rankPoints;

    @ElementCollection( fetch = FetchType.EAGER)
    @CollectionTable(name = "completed_lesson", joinColumns = @JoinColumn(name = "user_id"))
    private List<Integer> completedLessons = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "completed_week", joinColumns = @JoinColumn(name = "user_id"))
    private List<Integer> completedWeeks = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "completed_module", joinColumns = @JoinColumn(name = "user_id"))
    private List<Integer> completedModules = new ArrayList<>();

    @ElementCollection( fetch = FetchType.EAGER)
    @CollectionTable(name = "completed_kata", joinColumns = @JoinColumn(name = "user_id"))
    private List<Integer> completedKatas = new ArrayList<>();

    private String codeWarsUsername;
    private Integer rankPoints;
    private String profileImageUrl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
