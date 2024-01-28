package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface  ModuleRepository extends JpaRepository<Module,Integer> {
    List<Module> findAllByOrderByNumber();
}
