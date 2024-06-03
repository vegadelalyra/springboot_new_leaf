package com.lyradeperseo.springboot_new_leaf.student;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@AllArgsConstructor
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindStudentByEmail() {
        // Given
        String email = "mariam.jamal@gmail.com";
        Student student = new Student("Mariam", email, LocalDate.of(2000, Month.JANUARY, 1));
        underTest.save(student);

        // When
        Optional<Student> studentOptional = underTest.findStudentByEmail(email);

        // Then
        assertThat(studentOptional).isPresent();
        assertThat(studentOptional.get().getEmail()).isEqualTo(email);
    }

    @Test
    void itShouldNotFindStudentByEmail() {
        // Given
        String email = "not_existing@gmail.com";

        // When
        Optional<Student> studentOptional = underTest.findStudentByEmail(email);

        // Then
        assertThat(studentOptional).isNotPresent();
    }
}
