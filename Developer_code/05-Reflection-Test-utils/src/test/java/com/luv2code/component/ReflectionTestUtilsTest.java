package com.luv2code.component;

import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReflectionTestUtilsTest {

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades grades;

    @BeforeEach
    public void initialization() {

        student.setFirstname("Bhaskar");
        student.setLastname("Mudaliyar");
        student.setEmailAddress("mudaliyar@gmail.com");
        ReflectionTestUtils.setField(student, "id", 1);
    }


    @Test
    public void testPrivateFieldId() {

        assertEquals(1, ReflectionTestUtils.getField(student, "id"));
    }


    @Test
    public void testPrivateFieldMethod() {

        assertEquals("Bhaskar 1",  ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"));

    }
}