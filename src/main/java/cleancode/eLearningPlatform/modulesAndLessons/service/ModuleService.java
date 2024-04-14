package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@RequiredArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final WeekRepository weekRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<Module> findAllModules() {
        List<Module> modules = moduleRepository.findAllByOrderByNumber();


        for (int i = 0; i < modules.size(); i++) {
            List<Week> weeks = weekRepository.findAllByModuleIdOrderByNumber(modules.get(i).getId());

            modules.get(i).setWeeks(weeks
                                    .stream()
                                    .map((week) -> Week.builder()
                                                    .id(week.getId())
                                                    .number(week.getNumber())
                                                    .usersWithAccessWeek(week.getUsersWithAccessWeek())
                                                    .build()).toList());
        }

        return modules;
    }

    public Module findModuleById(int moduleId) {
        Module module = moduleRepository.findById(moduleId).orElse(null);
        List<Week> weeks = moduleRepository.getWeeksOfModuleById(module.getId());
        module.setWeeks(weeks);
        return module;
    }

    public Module saveModules(Module module) {
        return moduleRepository.save(module);
    }

    @Transactional
    @Modifying
    public void deleteModule(int moduleId) {
        List<User> users = userRepository.findAll();
        Module module = moduleRepository.findById(moduleId).orElse(null);

        userService.removeModuleFromAllUsers(module, true, users);

        moduleRepository.delete(moduleRepository.findById(moduleId).orElse(null));
        System.out.println("DELETE MODULE -> " + moduleId + "__________________________________");
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
