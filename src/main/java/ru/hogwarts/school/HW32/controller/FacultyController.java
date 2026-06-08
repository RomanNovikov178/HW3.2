package ru.hogwarts.school.HW32.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.HW32.model.Faculty;
import ru.hogwarts.school.HW32.service.FacultService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultService facultService;

    public FacultyController(FacultService facultService) {
        this.facultService = facultService;
    }

    @GetMapping("{id}")
    public ResponseEntity getFacultyInfo(@PathVariable long id) {
        Faculty faculty = facultService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getALLFaculty() {
        return ResponseEntity.ok(facultService.getALLFaculty());
    }

    @GetMapping("filter/{color}")
    public ResponseEntity getFacultyInfo(@PathVariable String color) {
        List<Faculty> facultyListColor = facultService.getFacultysColor(color);
        if (facultyListColor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyListColor);
    }

    @PostMapping
    public Faculty creatBook(@RequestBody Faculty faculty) {
        return facultService.creatFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteFaculty(@PathVariable Long id) {
        return facultService.deleteFaculty(id);
    }


}
