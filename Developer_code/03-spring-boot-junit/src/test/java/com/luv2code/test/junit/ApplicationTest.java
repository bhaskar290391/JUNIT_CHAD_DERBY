package com.luv2code.test.junit;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ApplicationTest {

    private static  int count=1;

    @Value("${info.app.name}")
    private String appName ;

    @Value("${info.app.description}")
    private String appDescription ;

    @Value("${info.app.version}")
    private String appVersion ;

    @Value("${info.school.name}")
    private String appSchoolName ;

    @Autowired
    private CollegeStudent collegeStudent;

    @Autowired
    private StudentGrades studentGrades;


    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void beforeEachMathod(){
        count=count+1;
        System.out.println("The app Name is "+appName +" which is "+appDescription+" having version "+appVersion);
        collegeStudent.setFirstname("Bhaskar");
        collegeStudent.setLastname("Mudaliyar");
        collegeStudent.setEmailAddress("mudaliyarbhaskar29@gmail.com");

        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0,20.0,40.0,40.0)));
        collegeStudent.setStudentGrades(studentGrades);
    }


    @Test
    public void testIsGreater(){
        assertTrue(studentGrades.isGradeGreater(75.0,35.4));
    }


    @Test
    public void testIsGreaterFalse(){
        assertFalse(studentGrades.isGradeGreater(10,35.4));
    }


    @Test
    public void testNull(){
        assertNotNull(studentGrades.checkNull(studentGrades.getMathGradeResults()));
    }

    @Test
    public void referenceCheck(){

        CollegeStudent student1=context.getBean("collegeStudent",collegeStudent.getClass());
        assertNotSame(student1,collegeStudent);
    }

    @Test
    public void studentWithoutGrades(){

        CollegeStudent student2=context.getBean("collegeStudent",collegeStudent.getClass());
        student2.setFirstname("Bhaskar1");
        student2.setLastname("Mudaliyar1");
        student2.setEmailAddress("mudaliyarbhaskar29@gmail.com1");

        assertNull(student2.getStudentGrades());
    }

    @Test
    public void TestFindGradeAverage(){
        assertEquals(50.0,studentGrades.findGradePointAverage(studentGrades.getMathGradeResults()));
    }

    @Test
    public void checkAddGradePoints(){
        assertEquals(200,studentGrades.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()));
    }

    @Test
    public void checkNotEqualAddGradePoints(){
        assertNotEquals(20,studentGrades.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()));
    }
}
