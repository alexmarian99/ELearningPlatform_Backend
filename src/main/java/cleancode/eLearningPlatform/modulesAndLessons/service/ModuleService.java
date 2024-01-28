package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public List<Module> findAllModules() {
        return moduleRepository.findAll();
    }

    public Module findModuleById(int moduleId) {
        return moduleRepository.findById(moduleId).orElse(null);

    }

    public Module saveModules(Module module) {
        return moduleRepository.save(module);
    }

    public String deleteModule(int moduleId) {
        moduleRepository.deleteById(moduleId);
        return "Deleted module " + moduleId ;
    }

    public Module updateModule(int moduleId, Module updatedModule) {
        Optional<Module> existingModuleOptional = moduleRepository.findById(moduleId);

        if (existingModuleOptional.isPresent()) {
            Module existingModule = existingModuleOptional.get();
            existingModule.setName(updatedModule.getName());
            existingModule.setNumber(updatedModule.getNumber());

            return moduleRepository.save(existingModule);
        } else {
            throw new IllegalArgumentException("Module not found with id: " + moduleId);
        }
    }
}
