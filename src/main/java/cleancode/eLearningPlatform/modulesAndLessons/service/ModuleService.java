package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final WeekService weekService;

    public List<Module> findAllModules() {
        return moduleRepository.findAllByOrderByNumber();
    }

    public Module findModuleById(int moduleId) {
        return moduleRepository.findById(moduleId).orElse(null);

    }

    public Module saveModules(Module module) {
        return moduleRepository.save(module);
    }

    @Transactional
    @Modifying
    public void deleteModule(int moduleId) {
        List<User> users = userRepository.findAll();
        Module module = moduleRepository.findById(moduleId).orElse(null);

        module.getWeeks().stream().forEach(week -> weekService.deleteWeekById(week.getId(), users));

        moduleRepository.deleteById(moduleId);
    }

    public Module updateModule(int moduleId, Module updatedModule) {
        Optional<Module> existingModuleOptional = moduleRepository.findById(moduleId);

        if (existingModuleOptional.isPresent()) {
            Module existingModule = existingModuleOptional.get();

            existingModule.setName(updatedModule.getName());
            existingModule.setNumber(updatedModule.getNumber());
            existingModule.setImgLink(updatedModule.getImgLink());

            return moduleRepository.save(existingModule);
        } else {
            throw new IllegalArgumentException("Module not found with id: " + moduleId);
        }
    }

}
