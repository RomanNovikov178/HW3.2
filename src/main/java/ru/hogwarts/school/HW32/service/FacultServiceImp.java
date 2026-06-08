package ru.hogwarts.school.HW32.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.HW32.model.Faculty;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultServiceImp implements FacultService {
    private long lastId = 0;

    public long getLastId() { /// временно!!!
        return lastId;
    }

    Map<Long, Faculty> facults = new HashMap<>();

    @Override
    public Faculty creatFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        facults.put(lastId, faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(long id) {
        return facults.get(id);
    }

    @Override
    public List<Faculty> getFacultysColor(String color) {
        List<Faculty> facultyColor = facults.entrySet().stream()
                .filter(entry -> entry.getValue().getColor().equals((color)))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        return facultyColor;
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        if (facults.containsKey(faculty.getId())) {
            facults.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    @Override
    public Faculty deleteFaculty(long id) {
        return facults.remove(id);
    }

    @Override
    public Collection<Faculty> getALLFaculty() {
        return facults.values();
    }
}
