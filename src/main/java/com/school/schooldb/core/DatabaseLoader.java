package com.school.schooldb.core;

import com.school.schooldb.model.Course;
import com.school.schooldb.model.Role;
import com.school.schooldb.model.RoleName;
import com.school.schooldb.model.User;
import com.school.schooldb.repository.CourseRepository;
import com.school.schooldb.repository.RoleRepository;
import com.school.schooldb.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseLoader implements CommandLineRunner {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private RoleRepository roleRepository;

    public DatabaseLoader(UserRepository userRepository, CourseRepository courseRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Set<Role> roles = new HashSet<>();

        Role adminRole = new Role();
        adminRole.setName(RoleName.ROLE_ADMIN);

        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);

        roles.add(adminRole);
        roles.add(userRole);

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        User testUserOne = new User(
                "Joe",
                "Smith",
                "joe@smith.com",
                PASSWORD_ENCODER.encode("password")
        );

        User testUserTwo = new User(
                "Sally",
                "Jones",
                "sally@jones.com",
                PASSWORD_ENCODER.encode("password")
        );

        testUserOne.setRoles(roles);
        testUserTwo.setRoles(roles);

        userRepository.save(testUserOne);
        userRepository.save(testUserTwo);

        Course c1 = new Course(
                "Build a Basic Bookcase",
                "High-end furniture projects are great to dream about. But unless you have a well-equipped shop and" +
                        " some serious woodworking experience to draw on, it can be difficult to turn the dream into " +
                        "a reality.\\n\\nNot every piece of furniture needs to be a museum showpiece, though. Often a" +
                        " simple design does the job just as well and the experience gained in completing it goes a " +
                        "long way toward making the next project even better.\\n\\nOur pine bookcase, for example, " +
                        "features simple construction and it's designed to be built with basic woodworking tools. " +
                        "Yet, the finished project is a worthy and useful addition to any room of the house. While " +
                        "it's meant to rest on the floor, you can convert the bookcase to a wall-mounted storage unit" +
                        " by leaving off the baseboard. You can secure the cabinet to the wall by screwing through " +
                        "the cabinet cleats into the wall studs.\\n\\nWe made the case out of materials available at " +
                        "most building-supply dealers and lumberyards, including 1/2 x 3/4-in. parting strip, 1 x 2, " +
                        "1 x 4 and 1 x 10 common pine and 1/4-in.-thick lauan plywood. Assembly is quick and easy " +
                        "with glue and nails, and when you're done with construction you have the option of a painted" +
                        " or clear finish.\\n\\nAs for basic tools, you'll need a portable circular saw, hammer, " +
                        "block plane, combination square, tape measure, metal rule, two clamps, nail set and putty " +
                        "knife. Other supplies include glue, nails, sandpaper, wood filler and varnish or paint and " +
                        "shellac.\\n\\nThe specifications that follow will produce a bookcase with overall dimensions" +
                        " of 10 3/4 in. deep x 34 in. wide x 48 in. tall. While the depth of the case is directly " +
                        "tied to the 1 x 10 stock, you can vary the height, width and shelf spacing to suit your " +
                        "needs. Keep in mind, though, that extending the width of the cabinet may require the " +
                        "addition of central shelf supports.",
                "12 hours",
                "* 1/2 x 3/4 inch parting strip\n* 1 x 2 common pine\n* 1 x 4 common pine\n* 1 x 10 common pine\n* 1/4 " +
                        "inch thick lauan plywood\n* Finishing Nails\n* Sandpaper\n* Wood Glue\n* Wood Filler\n* " +
                        "Minwax Oil Based Polyurethane\n"
        );

        c1.setUser(testUserOne);

        Course c2 = new Course(
                "Learn How to Program",
                "In this course, you'll learn how to write code like a pro!",
                "6 hours",
                "* Notebook computer running Mac OS X or Windows\n* Text editor"
        );

        c2.setUser(testUserTwo);

        courseRepository.save(c1);
        courseRepository.save(c2);

    }
}
