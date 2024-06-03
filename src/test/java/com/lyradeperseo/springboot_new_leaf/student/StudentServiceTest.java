package com.lyradeperseo.springboot_new_leaf.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService underTest;

    @Test
    void CanGetAllStudents() {
        // given
        Student student = new Student(
                "Mariam",
                "mariam.jamal@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 1));
        List<Student> students = List.of(student);
        when(studentRepository.findAll()).thenReturn(students);

        // when
        List<Student> expected = underTest.getStudents();

        // then
        assertThat(expected).isEqualTo(students);
        verify(studentRepository).findAll();
    }

    @Test
    void canAddNewStudent() {
        // given
        Student student = new Student(
                "Mariam",
                "mariam.jamal@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 1));
        when(
                studentRepository
                        .findStudentByEmail(student.getEmail()))
                        .thenReturn(Optional.empty());

        // when
        underTest.addNewStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        String existingEmail = "existing@gmail.com";
        Student existingStudent = new Student(
                "Existing",
                existingEmail,
                LocalDate.of(2000, Month.JANUARY, 1));
        Student student = new Student(
                "Mariam",
                existingEmail,
                LocalDate.of(2000, Month.JANUARY, 1));

        when(
                studentRepository
                        .findStudentByEmail(existingEmail))
                        .thenReturn(Optional.of(existingStudent));

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewStudent(student))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(existingEmail + " email taken already!");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudent() {
        // given
        Long studentId = 1L;

        when(studentRepository.existsById(studentId)).thenReturn(true);

        // when
        underTest.deleteStudent(studentId);

        // then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void willThrowWhenStudentDoesNotExist() {
        // given
        Long studentId = 1L;

        when(studentRepository.existsById(studentId)).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteStudent(studentId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("that student does not exist!");
    }

    @Test
    void canUpdateStudent() {
        // given
        Long studentId = 1L;
        Student student = new Student(
                "Mariam",
                "mariam.jamal@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 1));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.findStudentByEmail("newemail@gmail.com")).thenReturn(Optional.empty());

        // when
        underTest.updateStudent(studentId, "New Name", "newemail@gmail.com");

        // then
        assertThat(student.getName()).isEqualTo("New Name");
        assertThat(student.getEmail()).isEqualTo("newemail@gmail.com");
    }

    @Test
    void willThrowWhenUpdatingToExistingEmail() {
        // given
        Long studentId = 1L;
        Student student = new Student(
                "Mariam",
                "mariam.jamal@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 1));
        Student existingStudent = new Student(
                "Alex",
                "existingemail@gmail.com",
                LocalDate.of(2004, Month.OCTOBER, 5));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.findStudentByEmail("existingemail@gmail.com"))
                .thenReturn(Optional.of(existingStudent));

        // when
        // then
        assertThatThrownBy(() -> underTest.updateStudent(
                studentId, "New Name", "existingemail@gmail.com"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email existingemail@gmail.com taken");

        assertThat(student.getEmail()).isNotEqualTo("existingemail@gmail.com");
    }
}