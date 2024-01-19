package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public List<Module> findAllModules(){
        return moduleRepository.findAll();
    }
    public Module saveModules(Module module){
        return moduleRepository.save(module);
    }

}
