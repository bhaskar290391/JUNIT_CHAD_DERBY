package com.luv2code.springmvc;


import com.luv2code.springmvc.dao.HistoryGradeDao;
import com.luv2code.springmvc.dao.MathGradeDao;
import com.luv2code.springmvc.dao.ScienceGradeDao;
import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class SudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService service;

    @Autowired
    private StudentDao daoLayer;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    private MathGradeDao mathGradeDao;


    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    private ScienceGradeDao scienceGradeDao;


    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private HistoryGradeDao historyGradeDao;


    @Value("${create-student}")
    private String createStudent;


    @Value("${create-math-grade}")
    private String createMathGrade;

    @Value("${create-science-grade}")
    private String createScienceGrade;


    @Value("${create-history-grade}")
    private String createHistoryGrade;


    @Value("${delete-student}")
    private String deleteStudent;


    @Value("${delete-math-grade}")
    private String deleteMathGrade;


    @Value("${delete-science-grade}")
    private String deleteScienceGrade;


    @Value("${delete-history-grade}")
    private String deleteHistoryGrade;

    @BeforeEach
    public void setUpData() {
        jdbcTemplate.execute(createStudent);
        jdbcTemplate.execute(createMathGrade);
        jdbcTemplate.execute(createScienceGrade);
        jdbcTemplate.execute(createHistoryGrade);


    }

    @AfterEach
    public void setUpDataRemoval() {
        jdbcTemplate.execute(deleteStudent);
        jdbcTemplate.execute(deleteMathGrade);
        jdbcTemplate.execute(deleteScienceGrade);
        jdbcTemplate.execute(deleteHistoryGrade);


    }



    @Test
    public void createStudent() {
        service.createStudent("bhaskar", "mudaliyar", "bhaksar.mudaliyar@gmail.com");
        CollegeStudent byEmailAddress = daoLayer.findByEmailAddress("bhaksar.mudaliyar@gmail.com");
        assertEquals("bhaksar.mudaliyar@gmail.com", byEmailAddress.getEmailAddress());
    }


    @Test
    public void testStudentNullCheck() {
        assertTrue(service.checkStudentIfNull(1));
        assertFalse(service.checkStudentIfNull(0));
    }


    @Test
    public void deleteStudentService() {
        Optional<CollegeStudent> students = daoLayer.findById(1);
        Optional<MathGrade> mathStudent = mathGradeDao.findById(1);
        Optional<ScienceGrade> scienceStudent = scienceGradeDao.findById(1);
        Optional<HistoryGrade> historyStudent = historyGradeDao.findById(1);

        assertTrue(students.isPresent());
        assertTrue(mathStudent.isPresent());
        assertTrue(scienceStudent.isPresent());
        assertTrue(historyStudent.isPresent());


        service.deleteStudent(1);


        students = daoLayer.findById(1);
         mathStudent = mathGradeDao.findById(1);
        scienceStudent = scienceGradeDao.findById(1);
        historyStudent = historyGradeDao.findById(1);

        assertFalse(students.isPresent());
        assertFalse(mathStudent.isPresent());
        assertFalse(scienceStudent.isPresent());
        assertFalse(historyStudent.isPresent());
    }


    @Test
    public void getStudentInformation(){

        GradebookCollegeStudent studentInformation = service.getStudentInformation(1);

        assertEquals(1,studentInformation.getId());
        assertEquals("maddy",studentInformation.getFirstname());
        assertTrue(studentInformation.getStudentGrades().getMathGradeResults().size()==1);

    }

    @Test
    public void getStudentInformationWithInvalidStudent(){

        GradebookCollegeStudent studentInformation = service.getStudentInformation(0);

      assertNull(studentInformation);


    }

    @Test
    @Sql("/bhaskar.sql")
    public void testGradeBook() {

        Iterable<CollegeStudent> students = service.getGradebooks();
        List<CollegeStudent> studentsData = new ArrayList<>();
        for (CollegeStudent data : students) {
            studentsData.add(data);
        }

        assertEquals(5, studentsData.size());
    }




}
