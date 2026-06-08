package ru.hogwarts.school.HW32.service;

import ru.hogwarts.school.HW32.model.Faculty;
import ru.hogwarts.school.HW32.model.Student;

import java.util.Collection;
import java.util.List;

public interface FacultService {
    Faculty creatFaculty(Faculty faculty);

    List<Faculty> getFacultysColor(String color);

    Faculty findFaculty(long id);

    Faculty editFaculty(Faculty faculty);

    Faculty deleteFaculty(long id);

    Collection<Faculty> getALLFaculty();
}

