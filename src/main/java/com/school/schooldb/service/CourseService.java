package com.school.schooldb.service;

import com.school.schooldb.model.Course;
import com.school.schooldb.model.User;
import com.school.schooldb.repository.CourseRepository;
import com.school.schooldb.repository.RoleRepository;
import com.school.schooldb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    final
    CourseRepository courseRepository;

    final
    UserRepository userRepository;

    final
    RoleRepository roleRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, RoleRepository
            roleRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<Course> findAll() {return courseRepository.findAll();}

    public Course findById(Long id) {return courseRepository.findCourseById(id);}

    public Course createCourse(Course course, User currentUser) {
        Course _course = new Course(

                course.getTitle(),
                course.getDescription(),
                course.getEstimatedTime(),
                course.getMaterialsNeeded()
        );

        _course.setUser(currentUser);

        return courseRepository.save(_course);
    }

    public Course update(Course course, User user, Long id) {

        course.setId(id);

        course.setUser(user);

        courseRepository.save(course);

        return course;
    }

    public void delete(Course course) {

        courseRepository.delete(course);
    }

    public Course generateAddCourseMarkup(Course course) {
        String materialsNeeded = course.getMaterialsNeeded().replaceAll("[\r\n]+", "\n* ");

        materialsNeeded = "* " + materialsNeeded;

        course.setMaterialsNeeded(materialsNeeded);

        return course;
    }

    public Course generateEditCourseMarkup(Course course) {
        String materialsNeeded = course.getMaterialsNeeded().replace("* ", "");

        materialsNeeded = "\n" + materialsNeeded;

        materialsNeeded = materialsNeeded.replaceAll("[\r\n]+", "\n* ");

        course.setMaterialsNeeded(materialsNeeded);

        return course;
    }
}
