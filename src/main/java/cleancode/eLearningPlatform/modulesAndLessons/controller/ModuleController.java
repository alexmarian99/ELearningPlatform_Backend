package cleancode.eLearningPlatform.modulesAndLessons.controller;


import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/module")
public class ModuleController {

private final ModuleService moduleService;

    @GetMapping
    public List<Module> findAllModules(){
        return moduleService.findAllModules();
    }

    @PostMapping
    public Module saveModules(@RequestBody Module module){
        return moduleService.saveModules(module);
    }
}

