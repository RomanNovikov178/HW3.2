package ru.hogwarts.school.HW32.service;

import ru.hogwarts.school.HW32.model.Faculty;
import ru.hogwarts.school.HW32.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student creatStudent(Student student);

    List<Student> getStudentAge(int age);

    Student findStudent(long id);

    Student editStudent(Student student);

    Student deleteStudent(long id);

    Collection<Student> getALLFaculty();
}
