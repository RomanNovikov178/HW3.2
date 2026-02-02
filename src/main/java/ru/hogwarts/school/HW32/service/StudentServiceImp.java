package ru.hogwarts.school.HW32.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.HW32.model.Faculty;
import ru.hogwarts.school.HW32.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImp implements StudentService {

    private long lastId = 0;
    Map<Long, Student> students = new HashMap<>();

    @Override
    public Student creatStudent(Student student) {

        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    @Override
    public Student findStudent(long id) {
        return students.get(id);
    }

    @Override
    public Collection<Student> getALLFaculty() {
        return students.values();
    }

    @Override
    public List<Student> getStudentAge(int age) {
        List<Student> studentsAge = students.entrySet().stream()
                .filter(entry -> entry.getValue().getAge()==age)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        return studentsAge;
    }

    @Override
    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    @Override
    public Student deleteStudent(long id) {
        return students.remove(id);
    }


}
