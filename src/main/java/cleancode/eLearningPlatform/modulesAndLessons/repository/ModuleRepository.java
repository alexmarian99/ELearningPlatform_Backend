package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ModuleRepository extends JpaRepository<Module,Integer> {

}
