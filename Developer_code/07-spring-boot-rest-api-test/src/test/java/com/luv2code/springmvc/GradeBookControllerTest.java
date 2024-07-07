package com.luv2code.springmvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class GradeBookControllerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    @Autowired
    private static MockHttpServletRequest request;

    @Autowired
    private EntityManager manager;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentServiceMock;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CollegeStudent student;


    @BeforeAll
    public static void initializeRequest(){
        request=new MockHttpServletRequest();
        request.setParameter("firstName","Bhaskar");
        request.setParameter("lastName","Mudaliyar>");
        request.setParameter("emailAddress","Bhaskar@gmail.com");
    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @Test
    public void getStudentTest() throws  Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(1)));


    }


    @Test
    public void createStudent() throws Exception {

        student.setFirstname("maddy");
        student.setLastname("mudaliyar");
        student.setEmailAddress("maddy@gmail.com");
        mockMvc.perform( post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));

    }


    @Test
    public void deleteStudent() throws  Exception{
       assertTrue(studentDao.findById(1).isPresent());

       mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}",1))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$",hasSize(0)));

       assertFalse(studentDao.findById(1).isPresent());

    }



    @Test
    public void deleteStudentWhichDoesNotExists() throws  Exception{
        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}",0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));
    }


    @Test
    public void getStudentInformation() throws  Exception{

        assertTrue(studentDao.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.firstname",is("Eric")));
    }



    @Test
    public void getStudentInformationDoesNotExists() throws  Exception{

        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));
    }




    @Test
    public void createValidGrade() throws  Exception{

        mockMvc.perform(post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade","85.0")
                .param("gradeType","math")
                .param("studentId","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults",hasSize(2)));
    }


    @Test
    public void createValidGradeForInvalidStudent() throws  Exception{

        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","85.0")
                        .param("gradeType","math")
                        .param("studentId","5"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                        .andExpect(jsonPath("$.message",is("Student or Grade was not found")));
    }

    @Test
    public void createValidGradeForInvalidGradeType() throws  Exception{

        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","85.0")
                        .param("gradeType","Bhaskar")
                        .param("studentId","1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));
    }


    @Test
    public void deleteGrade() throws  Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}",1,"math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentGrades.mathGradeResults",hasSize(0)));
    }


    @Test
    public void deleteGradeByInvalidGradeId() throws  Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}",2,"math"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)));
    }

    @Test
    public void deleteGradeByInvalidGradeType() throws  Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}",1,"Bhaskar"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)));
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }
}
