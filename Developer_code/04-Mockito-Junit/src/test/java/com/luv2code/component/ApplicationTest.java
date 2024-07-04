package com.luv2code.component;

import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApplicationTest {

    //@Mock
    @MockBean
    private ApplicationDao appDao;

    //@InjectMocks
    @Autowired
    private ApplicationService service;


    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades grades;

    @Autowired
    private ApplicationContext context;

    @BeforeEach()
    public void initialization(){
        grades.setMathGradeResults(new ArrayList<>(Arrays.asList(130.2,25.0)));
        student.setStudentGrades(grades);
    }

    @Test
    @DisplayName("When mockito")
    public void testApplicationGrades(){
        when(appDao.addGradeResultsForSingleClass(grades.getMathGradeResults())).thenReturn(100.0);
        assertEquals(100.0,service.addGradeResultsForSingleClass(grades.getMathGradeResults()));
    }


    @Test
    @DisplayName("GDP")
    public void testGDP(){
        when(appDao.findGradePointAverage(student.getStudentGrades().getMathGradeResults())).thenReturn(81.5);
        assertEquals(81.5,service.findGradePointAverage(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    @DisplayName("Throw Exception")
    public void throwException(){
        CollegeStudent student2= (CollegeStudent) context.getBean("collegeStudent");

        when(appDao.checkNull(student2.getStudentGrades())).thenThrow(new RuntimeException()).thenReturn("Do not throw Exception");

        assertThrows(RuntimeException.class,()->{service.checkNull(student2.getStudentGrades());});

        assertEquals("Do not throw Exception",service.checkNull(student2.getStudentGrades()));
    }
}
