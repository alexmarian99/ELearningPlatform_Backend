package cleancode.eLearningPlatform.courses.service;

import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.courses.model.Course;
import cleancode.eLearningPlatform.courses.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course findCourseById(int courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Response deleteCourseById(int courseId) {
        courseRepository.deleteById(courseId);
        String response = courseRepository.existsById(courseId) ? "Delete done" : "Delete failed";
        return Response.builder().response(response).build();
    }

    public Course editCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }
}
