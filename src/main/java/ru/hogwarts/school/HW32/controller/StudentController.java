package ru.hogwarts.school.HW32.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.HW32.model.Faculty;
import ru.hogwarts.school.HW32.model.Student;

import ru.hogwarts.school.HW32.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity getStudentsInfo(@PathVariable long id) {
        Student student= studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("filter/{age}")
    public ResponseEntity getStudentInfo(@PathVariable int age) {
        List<Student> studentsListAge = studentService.getStudentAge(age);
        if (studentsListAge.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentsListAge);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getALLStudents() {
        return ResponseEntity.ok(studentService.getALLFaculty());
    }

    @PostMapping
    public Student creatBook(@RequestBody Student student) {
        return studentService.creatStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public Student deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }


}
