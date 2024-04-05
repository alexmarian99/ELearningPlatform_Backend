package cleancode.eLearningPlatform.courses.controller;

import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.courses.model.Course;
import cleancode.eLearningPlatform.courses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("course")
@RequiredArgsConstructor
@RestController
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable int courseId){
        return ResponseEntity.ok(courseService.findCourseById(courseId));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll(){
        return ResponseEntity.ok(courseService.getAll());
    }

    @PostMapping
    public ResponseEntity<Course> saveCourse(@RequestBody Course course){
        return ResponseEntity.ok(courseService.saveCourse(course));
    }

    @PutMapping
    public ResponseEntity<Course> editCourse(@RequestBody Course course){
        return ResponseEntity.ok(courseService.editCourse(course));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Response> deleteCourseById(@PathVariable int courseId){
        return ResponseEntity.ok(courseService.deleteCourseById(courseId));
    }
}
