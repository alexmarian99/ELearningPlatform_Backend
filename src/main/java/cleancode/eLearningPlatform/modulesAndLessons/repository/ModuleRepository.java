package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface  ModuleRepository extends JpaRepository<Module,Integer> {
    List<Module> findAllByOrderByNumber();

    @Query("SELECT m.weeks FROM Module m WHERE m.id = :moduleId")
    List<Week> getWeeksOfModuleById(int moduleId);
}
