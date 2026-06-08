package ru.hogwarts.school.HW32.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.HW32.model.Faculty;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TestFacultyServiceImp {

    private FacultServiceImp out;

    @BeforeEach
    void setUp() {
        out = new FacultServiceImp();
    }

    @Test
    @DisplayName("Тест: creatFaculty - успешное создание факультета")
    void creatFacultyTest() {

        Faculty faculty = new Faculty(null, "Gryffindor", "Red");
        Faculty createdFaculty = out.creatFaculty(faculty);

        assertNotNull(createdFaculty);
        assertNotNull(createdFaculty.getId());
        assertTrue(createdFaculty.getId() > 0);
        assertEquals("Gryffindor", createdFaculty.getName());
        assertEquals("Red", createdFaculty.getColor());
        assertNotNull(out.facults);
    }

    @Test
    @DisplayName("Тест: findFaculty - успешный поиск существующего факультета")
    void findFacultyNoNull() {
        Faculty faculty = new Faculty(null, "Slytherin", "Green");
        Faculty createdFaculty = out.creatFaculty(faculty);

        Faculty foundFaculty = out.findFaculty(createdFaculty.getId());

        assertNotNull(foundFaculty);
        assertEquals(createdFaculty, foundFaculty);
    }
    @Test
    @DisplayName("Тест: findFaculty - поиск несуществующего факультета")
    void findFacultyShouldReturnNull() {
        Faculty foundFaculty = out.findFaculty(999L);

        assertNull(foundFaculty);
    }

    @Test
    @DisplayName("Тест: getFacultysColor - поиск факультетов по существующему цвету (чувствительно к регистру)")
    void getFacultysColor_shouldReturnFacultiesWithGivenColorCaseSensitive() {
        Faculty gryffindor = out.creatFaculty(new Faculty(null, "Gryffindor", "Red"));
        Faculty slytherin = out.creatFaculty(new Faculty(null, "Slytherin", "Green"));
        Faculty hufflepuff = out.creatFaculty(new Faculty(null, "Hufflepuff", "Yellow"));
        Faculty anotherGryffindor = out.creatFaculty(new Faculty(null, "New Gryffindor", "Red"));
        Faculty lowerCaseRed = out.creatFaculty(new Faculty(null, "Lower Red", "red")); // Отличается регистром

        List<Faculty> redFaculties = out.getFacultysColor("Red");

        assertNotNull(redFaculties);
        assertEquals(2, redFaculties.size());
        assertTrue(redFaculties.contains(gryffindor));
        assertTrue(redFaculties.contains(anotherGryffindor));
        assertFalse(redFaculties.contains(slytherin));
        assertFalse(redFaculties.contains(lowerCaseRed));
    }

    @Test
    @DisplayName("Тест: getFacultysColor - поиск по несуществующему цвету")
    void getFacultysColor_shouldReturnEmptyListWhenColorNotExists() {
        out.creatFaculty(new Faculty(null, "Gryffindor", "Red"));
        out.creatFaculty(new Faculty(null, "Slytherin", "Green"));

        List<Faculty> blueFaculties = out.getFacultysColor("Blue");

        assertNotNull(blueFaculties);
        assertTrue(blueFaculties.isEmpty()); // Список должен быть пустым
    }

    @Test
    @DisplayName("Тест: getFacultysColor - поиск с null или пустым цветом")
    void getFacultysColor_shouldReturnEmptyListWhenColorIsNullOrEmpty() {

        out.creatFaculty(new Faculty(null, "Gryffindor", "Red"));

        List<Faculty> nullColorFaculties = out.getFacultysColor(null);
        assertNotNull(nullColorFaculties);
        assertTrue(nullColorFaculties.isEmpty());

        List<Faculty> emptyColorFaculties = out.getFacultysColor("");
        assertNotNull(emptyColorFaculties);
        assertTrue(emptyColorFaculties.isEmpty());

        List<Faculty> blankColorFaculties = out.getFacultysColor("   ");
        assertNotNull(blankColorFaculties);
        assertTrue(blankColorFaculties.isEmpty());
    }

    @Test
    @DisplayName("Тест: editFaculty - успешное обновление существующего факультета")
    void editFaculty_shouldReturnUpdatedFacultyWhenExists() {
        // Подготовка: создаем факультет
        Faculty originalFaculty = out.creatFaculty(new Faculty(null, "Ravenclaw", "Blue"));

        // Изменяем данные факультета
        Faculty updatedFaculty = new Faculty(originalFaculty.getId(), "Ravenclaw New", "Indigo");

        // Выполнение тестируемого метода
        Faculty result = out.editFaculty(updatedFaculty);

        // Проверки
        assertNotNull(result);
        assertEquals(updatedFaculty.getId(), result.getId());
        assertEquals("Ravenclaw New", result.getName());
        assertEquals("Indigo", result.getColor());

        // Проверяем, что изменения сохранились в хранилище
        Faculty foundFaculty = out.findFaculty(originalFaculty.getId());
        assertNotNull(foundFaculty);
        assertEquals("Ravenclaw New", foundFaculty.getName());
        assertEquals("Indigo", foundFaculty.getColor());
    }

    @Test
    @DisplayName("Тест: editFaculty - обновление несуществующего факультета")
    void editFaculty_shouldReturnNullWhenNotExists() {
        // Подготовка: создаем факультет с несуществующим ID
        Faculty nonExistentFaculty = new Faculty(999L, "NonExistent", "Black");

        // Выполнение тестируемого метода
        Faculty result = out.editFaculty(nonExistentFaculty);

        // Проверки
        assertNull(result); // Должен вернуть null, так как факультет не найден
    }

    @Test
    @DisplayName("Тест: editFaculty - обновление с null факультетом (ожидается NPE)")
    void editFaculty_shouldThrowNPEWhenFacultyIsNull() {
        // В оригинальном коде нет проверки на null, поэтому ожидаем NullPointerException
        assertThrows(NullPointerException.class, () -> out.editFaculty(null));
    }

    @Test
    @DisplayName("Тест: editFaculty - обновление с null ID факультета (ожидается NPE)")
    void editFaculty_shouldThrowNPEWhenFacultyIdIsNull() {
        // В оригинальном коде нет проверки на null ID, поэтому ожидаем NullPointerException
        assertThrows(NullPointerException.class, () -> out.editFaculty(new Faculty(null, "Name", "Color")));
    }

    @Test
    @DisplayName("Тест: deleteFaculty - успешное удаление существующего факультета")
    void deleteFaculty_shouldReturnDeletedFacultyWhenExists() {
        // Подготовка: создаем факультет
        Faculty facultyToDelete = out.creatFaculty(new Faculty(null, "Hufflepuff", "Yellow"));
        long idToDelete = facultyToDelete.getId();

        // Выполнение тестируемого метода
        Faculty deletedFaculty = out.deleteFaculty(idToDelete);

        // Проверки
        assertNotNull(deletedFaculty);
        assertEquals(facultyToDelete, deletedFaculty); // Возвращенный объект должен совпадать с удаленным

        // Проверяем, что факультет действительно был удален из хранилища
        Faculty foundFaculty = out.findFaculty(idToDelete);
        assertNull(foundFaculty); // Факультет больше не должен быть найден
    }

    @Test
    @DisplayName("Тест: deleteFaculty - удаление несуществующего факультета")
    void deleteFaculty_shouldReturnNullWhenNotExists() {
        // Выполнение тестируемого метода с несуществующим ID
        Faculty result = out.deleteFaculty(999L); // ID, который точно не существует

        // Проверки
        assertNull(result); // Должен вернуть null
    }

    @Test
    @DisplayName("Тест: getALLFaculty - получение всех факультетов, когда они есть")
    void getALLFaculty_shouldReturnAllFacultiesWhenSomeExist() {
        // Подготовка: создаем несколько факультетов
        Faculty f1 = out.creatFaculty(new Faculty(null, "Gryffindor", "Red"));
        Faculty f2 = out.creatFaculty(new Faculty(null, "Slytherin", "Green"));
        Faculty f3 = out.creatFaculty(new Faculty(null, "Hufflepuff", "Yellow"));

        // Выполнение тестируемого метода
        Collection<Faculty> allFaculties = out.getALLFaculty();

        // Проверки
        assertNotNull(allFaculties);
        assertEquals(3, allFaculties.size()); // Должно быть 3 факультета
        assertTrue(allFaculties.contains(f1));
        assertTrue(allFaculties.contains(f2));
        assertTrue(allFaculties.contains(f3));
    }

    @Test
    @DisplayName("Тест: getALLFaculty - получение всех факультетов, когда их нет")
    void getALLFaculty_shouldReturnEmptyCollectionWhenNoFaculties() {
        // Выполнение тестируемого метода (хранилище пустое)
        Collection<Faculty> allFaculties = out.getALLFaculty();

        // Проверки
        assertNotNull(allFaculties);
        assertTrue(allFaculties.isEmpty()); // Коллекция должна быть пустой
    }


    @Test
    @DisplayName("Тест: getLastId - проверка генерации ID")
    void getLastId_shouldReturnCorrectMaxId() {
        // Изначально ID должен быть 0
        assertEquals(0, out.getLastId());

        // После создания первого факультета ID должен стать 1
        out.creatFaculty(new Faculty(null, "F1", "C1"));
        assertEquals(1, out.getLastId());

        // После создания второго факультета ID должен стать 2
        out.creatFaculty(new Faculty(null, "F2", "C2"));
        assertEquals(2, out.getLastId());
    }

}

