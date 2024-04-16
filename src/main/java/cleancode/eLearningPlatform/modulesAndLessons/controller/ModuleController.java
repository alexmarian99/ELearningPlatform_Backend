package cleancode.eLearningPlatform.modulesAndLessons.controller;


import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/modules")
public class ModuleController {

private final ModuleService moduleService;

    @GetMapping
    public List<Module> findAllModules(){
        return moduleService.findAllModules();
    }

    @GetMapping("/{moduleId}")
    public Module findModuleById(@PathVariable int moduleId){
        return moduleService.findModuleById(moduleId);
    }

    @PostMapping
    public Module saveModules(@RequestBody Module module, @RequestHeader("Authorization") String authHeader){
        return moduleService.saveModules(module, authHeader);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteModule(@RequestParam (name = "moduleId") int moduleId, @RequestHeader("Authorization") String authHeader){
        moduleService.deleteModule(moduleId, authHeader);
        return ResponseEntity.ok(Response.builder().response("Deleted module " + moduleId).build());
    }

    @PutMapping("/{moduleId}")
    public Module updateModule(@PathVariable int moduleId, @RequestBody Module updatedModule, @RequestHeader("Authorization") String authHeader){
        return moduleService.updateModule(moduleId,updatedModule, authHeader);
    }
}

