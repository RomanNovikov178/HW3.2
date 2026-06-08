package ru.hogwarts.school.HW32.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.HW32.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestStudentServiceImp {
    private StudentServiceImp out;

    @BeforeEach
    void setUp() {
        out = new StudentServiceImp();
    }

    @Test
    @DisplayName("Тест: creatStudent - успешное создание студента")
    void creatStudent_shouldReturnCreatedStudentWithId() {
        // Подготовка: ID может быть null в конструкторе, так как поле Long
        Student student = new Student(null, "Harry Potter", 11);

        // Выполнение
        Student createdStudent = out.creatStudent(student);

        // Проверки
        assertNotNull(createdStudent, "Созданный студент не должен быть null");
        // createdStudent.getId() теперь возвращает long, поэтому assertNotNull для него не имеет смысла
        // Проверяем, что ID был присвоен и он положительный
        assertTrue(createdStudent.getId() > 0, "ID должен быть положительным числом");
        assertEquals("Harry Potter", createdStudent.getName(), "Имя студента должно совпадать");
        assertEquals(11, createdStudent.getAge(), "Возраст студента должен совпадать");

        // Проверка, что студент действительно добавлен в хранилище (через findStudent)
        Student foundStudent = out.findStudent(createdStudent.getId());
        assertNotNull(foundStudent, "Найденный студент не должен быть null после создания");
        // Сравниваем поля, так как Student не имеет equals()/hashCode()
        assertEquals(createdStudent.getId(), foundStudent.getId(), "ID найденного студента должен совпадать");
        assertEquals(createdStudent.getName(), foundStudent.getName(), "Имя найденного студента должно совпадать");
        assertEquals(createdStudent.getAge(), foundStudent.getAge(), "Возраст найденного студента должен совпадать");
    }

    @Test
    @DisplayName("Тест: creatStudent - создание студента с null (ожидается NPE)")
    void creatStudent_shouldThrowNPEWhenStudentIsNull() {
        // В вашем коде StudentServiceImp нет проверки на null для входящего Student,
        // поэтому ожидаем NullPointerException при попытке вызвать student.setId()
        assertThrows(NullPointerException.class, () -> out.creatStudent(null),
                "Ожидается NullPointerException при передаче null в creatStudent");
    }

    @Test
    @DisplayName("Тест: findStudent - успешный поиск существующего студента")
    void findStudent_shouldReturnStudentWhenExists() {
        // Подготовка
        Student student = new Student(null, "Hermione Granger", 11);
        Student createdStudent = out.creatStudent(student);

        // Выполнение
        Student foundStudent = out.findStudent(createdStudent.getId());

        // Проверки
        assertNotNull(foundStudent, "Найденный студент не должен быть null");
        // Сравниваем поля, так как Student не имеет equals()/hashCode()
        assertEquals(createdStudent.getId(), foundStudent.getId(), "ID найденного студента должен совпадать");
        assertEquals(createdStudent.getName(), foundStudent.getName(), "Имя найденного студента должно совпадать");
        assertEquals(createdStudent.getAge(), foundStudent.getAge(), "Возраст найденного студента должен совпадать");
    }

    @Test
    @DisplayName("Тест: findStudent - поиск несуществующего студента")
    void findStudent_shouldReturnNullWhenNotExists() {
        // Выполнение
        Student foundStudent = out.findStudent(999L); // ID, который точно не существует

        // Проверки
        assertNull(foundStudent, "Поиск несуществующего студента должен вернуть null");
    }

    @Test
    @DisplayName("Тест: getStudentAge - поиск студентов по существующему возрасту")
    void getStudentAge_shouldReturnStudentsWithGivenAge() {
        // Подготовка
        Student s1 = out.creatStudent(new Student(null, "Harry Potter", 11));
        Student s2 = out.creatStudent(new Student(null, "Ron Weasley", 11));
        Student s3 = out.creatStudent(new Student(null, "Hermione Granger", 11));
        Student s4 = out.creatStudent(new Student(null, "Draco Malfoy", 12));
        Student s5 = out.creatStudent(new Student(null, "Luna Lovegood", 11));

        // Выполнение
        List<Student> studentsAge11 = out.getStudentAge(11);

        // Проверки
        assertNotNull(studentsAge11, "Список найденных студентов не должен быть null");
        assertEquals(4, studentsAge11.size(), "Должно быть найдено 4 студента возрастом 11 лет");

        // Поскольку нет equals/hashCode, проверяем наличие по ID
        List<Long> foundIds = studentsAge11.stream().map(Student::getId).collect(Collectors.toList());
        assertTrue(foundIds.contains(s1.getId()), "Список должен содержать ID Harry Potter");
        assertTrue(foundIds.contains(s2.getId()), "Список должен содержать ID Ron Weasley");
        assertTrue(foundIds.contains(s3.getId()), "Список должен содержать ID Hermione Granger");
        assertTrue(foundIds.contains(s5.getId()), "Список должен содержать ID Luna Lovegood");
        assertFalse(foundIds.contains(s4.getId()), "Список не должен содержать ID Draco Malfoy");
    }

    @Test
    @DisplayName("Тест: getStudentAge - поиск по несуществующему возрасту")
    void getStudentAge_shouldReturnEmptyListWhenAgeNotExists() {
        // Подготовка
        out.creatStudent(new Student(null, "Harry Potter", 11));
        out.creatStudent(new Student(null, "Draco Malfoy", 12));

        // Выполнение
        List<Student> studentsAge10 = out.getStudentAge(10);

        // Проверки
        assertNotNull(studentsAge10, "Список найденных студентов не должен быть null");
        assertTrue(studentsAge10.isEmpty(), "Список должен быть пустым при поиске несуществующего возраста");
    }

    @Test
    @DisplayName("Тест: editStudent - успешное обновление существующего студента")
    void editStudent_shouldReturnUpdatedStudentWhenExists() {
        // Подготовка
        Student originalStudent = out.creatStudent(new Student(null, "Neville Longbottom", 11));
        long studentId = originalStudent.getId(); // ID теперь long

        // Создаем новый объект с измененными данными, но тем же ID
        Student updatedStudentData = new Student(studentId, "Neville Longbottom Jr.", 12);

        // Выполнение
        Student result = out.editStudent(updatedStudentData);

        // Проверки
        assertNotNull(result, "Результат редактирования не должен быть null");
        assertEquals(studentId, result.getId(), "ID отредактированного студента должен совпадать");
        assertEquals("Neville Longbottom Jr.", result.getName(), "Имя студента должно быть обновлено");
        assertEquals(12, result.getAge(), "Возраст студента должен быть обновлен");

        // Проверяем, что изменения сохранились в хранилище
        Student foundStudent = out.findStudent(studentId);
        assertNotNull(foundStudent, "Найденный студент после редактирования не должен быть null");
        assertEquals("Neville Longbottom Jr.", foundStudent.getName(), "Имя в хранилище должно быть обновлено");
        assertEquals(12, foundStudent.getAge(), "Возраст в хранилище должен быть обновлен");
    }

    @Test
    @DisplayName("Тест: editStudent - обновление несуществующего студента")
    void editStudent_shouldReturnNullWhenNotExists() {
        // Подготовка: создаем объект для обновления с несуществующим ID
        Student nonExistentStudent = new Student(999L, "NonExistent", 10);

        // Выполнение
        Student result = out.editStudent(nonExistentStudent);

        // Проверки
        assertNull(result, "Редактирование несуществующего студента должно вернуть null");
    }

    @Test
    @DisplayName("Тест: editStudent - обновление с null студентом (ожидается NPE)")
    void editStudent_shouldThrowNPEWhenStudentIsNull() {
        // В вашем коде StudentServiceImp нет проверки на null для входящего Student,
        // поэтому ожидаем NullPointerException при попытке вызвать student.getId()
        assertThrows(NullPointerException.class, () -> out.editStudent(null),
                "Ожидается NullPointerException при передаче null в editStudent");
    }

    @Test
    @DisplayName("Тест: editStudent - обновление с null ID студента (ожидается NPE)")
    void editStudent_shouldThrowNPEWhenStudentIdIsNull() {
        // Создаем студента, у которого поле 'id' равно null.
        // При вызове student.getId() в сервисе произойдет NullPointerException
        // из-за попытки распаковки null в примитив long.
        assertThrows(NullPointerException.class, () -> out.editStudent(new Student(null, "Name", 10)),
                "Ожидается NullPointerException при передаче студента с null ID в editStudent");
    }

    @Test
    @DisplayName("Тест: deleteStudent - успешное удаление существующего студента")
    void deleteStudent_shouldReturnDeletedStudentWhenExists() {
        // Подготовка
        Student studentToDelete = out.creatStudent(new Student(null, "Seamus Finnigan", 11));
        long idToDelete = studentToDelete.getId();

        // Выполнение
        Student deletedStudent = out.deleteStudent(idToDelete);

        // Проверки
        assertNotNull(deletedStudent, "Удаленный студент не должен быть null");
        // Сравниваем поля, так как Student не имеет equals()/hashCode()
        assertEquals(studentToDelete.getId(), deletedStudent.getId(), "ID удаленного студента должен совпадать");
        assertEquals(studentToDelete.getName(), deletedStudent.getName(), "Имя удаленного студента должно совпадать");
        assertEquals(studentToDelete.getAge(), deletedStudent.getAge(), "Возраст удаленного студента должен совпадать");

        // Проверяем, что студент действительно был удален из хранилища
        Student foundStudent = out.findStudent(idToDelete);
        assertNull(foundStudent, "Студент больше не должен быть найден после удаления");
    }

    @Test
    @DisplayName("Тест: deleteStudent - удаление несуществующего студента")
    void deleteStudent_shouldReturnNullWhenNotExists() {
        // Выполнение
        Student result = out.deleteStudent(999L); // ID, который точно не существует

        // Проверки
        assertNull(result, "Удаление несуществующего студента должно вернуть null");
    }

    @Test
    @DisplayName("Тест: getALLFaculty - получение всех студентов, когда они есть")
    void getALLFaculty_shouldReturnAllStudentsWhenSomeExist() {
        // Подготовка
        Student s1 = out.creatStudent(new Student(null, "Harry Potter", 11));
        Student s2 = out.creatStudent(new Student(null, "Ron Weasley", 11));
        Student s3 = out.creatStudent(new Student(null, "Hermione Granger", 11));

        // Выполнение
        Collection<Student> allStudents = out.getALLFaculty();

        // Проверки
        assertNotNull(allStudents, "Коллекция всех студентов не должна быть null");
        assertEquals(3, allStudents.size(), "Должно быть 3 студента в коллекции");

        // Поскольку нет equals/hashCode, проверяем наличие по ID
        List<Long> foundIds = allStudents.stream().map(Student::getId).collect(Collectors.toList());
        assertTrue(foundIds.contains(s1.getId()), "Коллекция должна содержать ID s1");
        assertTrue(foundIds.contains(s2.getId()), "Коллекция должна содержать ID s2");
        assertTrue(foundIds.contains(s3.getId()), "Коллекция должна содержать ID s3");
    }

    @Test
    @DisplayName("Тест: getALLFaculty - получение всех студентов, когда их нет")
    void getALLFaculty_shouldReturnEmptyCollectionWhenNoStudents() {
        // Выполнение (хранилище пустое)
        Collection<Student> allStudents = out.getALLFaculty();

        // Проверки
        assertNotNull(allStudents, "Коллекция всех студентов не должна быть null");
        assertTrue(allStudents.isEmpty(), "Коллекция должна быть пустой, когда студентов нет");
    }

    @Test
    @DisplayName("Тест: getLastId - проверка генерации ID")
    void getLastId_shouldReturnCorrectMaxId() {
        // Изначально ID должен быть 0
        assertEquals(0, out.lastId, "Изначальный lastId должен быть 0");

        // После создания первого студента ID должен стать 1
        out.creatStudent(new Student(null, "S1", 10));
        assertEquals(1, out.lastId, "lastId после создания одного студента должен быть 1");

        // После создания второго студента ID должен стать 2
        out.creatStudent(new Student(null, "S2", 11));
        assertEquals(2, out.lastId, "lastId после создания двух студентов должен быть 2");
    }
}
