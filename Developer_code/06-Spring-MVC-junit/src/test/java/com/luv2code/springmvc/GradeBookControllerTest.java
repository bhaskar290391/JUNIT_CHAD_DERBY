package com.luv2code.springmvc;


import com.luv2code.springmvc.dao.HistoryGradeDao;
import com.luv2code.springmvc.dao.MathGradeDao;
import com.luv2code.springmvc.dao.ScienceGradeDao;
import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
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
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GradeBookControllerTest {


    @Autowired
    private StudentAndGradeService service;

    @Mock
    private StudentAndGradeService serviceMock;

    @Autowired
    private MockMvc mockMvc;


    private static MockHttpServletRequest request;


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;


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

    @Autowired
    private StudentDao student;

    @BeforeAll
    public static void requestSetUp(){
        request=new MockHttpServletRequest();
        request.setParameter("firstName","bhaskar");
        request.setParameter("lastName","Mudaliyar");
        request.setParameter("emailAddress","bhaskar.mudaliyar@gmail.com");
    }

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
    public void testStudentHttpRequest() throws  Exception{

        CollegeStudent stu=new GradebookCollegeStudent("kanishk","maddy","bhaksar.kanishk");
        CollegeStudent stu1=new GradebookCollegeStudent("sammy","maddy","bhaksar.kanishk");

        List<CollegeStudent> student=new ArrayList<>(Arrays.asList(stu,stu1));

        when(serviceMock.getGradebooks()).thenReturn(student);

        assertEquals(student,serviceMock.getGradebooks());


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk()).andReturn();

        ModelAndViewAssert.assertViewName(mvcResult.getModelAndView(),"index");
    }


    @Test
    public void createStudentTest() throws  Exception{
        MvcResult mvcResult = mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON)
                .param("firstName", request.getParameter("firstName"))
                .param("lastName", request.getParameter("lastName"))
                .param("emailAddress", request.getParameter("emailAddress"))).andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");

    }


    @Test
    public void deleteStudentTest() throws  Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");

    }


    @Test
    public void deleteStudentTestErrorPage() throws  Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");

    }


    @Test
    public void createGrade() throws  Exception{

        assertTrue(service.createGrade(85.0,1,"math"));
        assertTrue(service.createGrade(85.0,1,"science"));
        assertTrue(service.createGrade(85.0,1,"history"));

        Iterable<MathGrade> mathGradeByStudentId = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGradesByStudentId = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGradeByStudentId = historyGradeDao.findGradeByStudentId(1);

        assertTrue(mathGradeByStudentId.iterator().hasNext());
        assertTrue(scienceGradesByStudentId.iterator().hasNext());
        assertTrue(historyGradeByStudentId.iterator().hasNext());


        assertTrue(((Collection<MathGrade>)mathGradeByStudentId).size()==2);
        assertTrue(((Collection<ScienceGrade>)scienceGradesByStudentId).size()==2);
        assertTrue(((Collection<HistoryGrade>)historyGradeByStudentId).size()==2);
    }



    @Test
    public void createGradeWithReturnFalse() throws  Exception{

        assertFalse(service.createGrade(1050.0,1,"math"));
        assertFalse(service.createGrade(-5.0,1,"science"));
        assertFalse(service.createGrade(85.0,2,"history"));
        assertFalse(service.createGrade(85.0,1,"bhaskar"));


    }


    @Test
    public void deleteGrade(){
        assertEquals(1,service.deletGrade(1,"math"));
        assertEquals(1,service.deletGrade(1,"science"));
        assertEquals(1,service.deletGrade(1,"history"));
    }


    @Test
    public void deleteGradeWithInvalidStudentIdAndInvalidSubject(){
        assertEquals(0,service.deletGrade(0,"math"));
        assertEquals(0,service.deletGrade(1,"English"));
    }


    @Test
    public void createStudentInformationWithValidStudentId() throws  Exception{

        assertTrue(student.findById(1).isPresent());

        ModelAndView modelAndView = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"studentInformation");
    }


    @Test
    public void createStudentInformationWithInValidStudentId() throws  Exception{

        assertFalse(student.findById(0).isPresent());

        ModelAndView modelAndView = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }


    @Test
    public void createGradeController() throws  Exception{

       assertTrue(student.findById(1).isPresent());
       assertEquals(1, service.getStudentInformation(1).getStudentGrades().getMathGradeResults().size());

        ModelAndView modelAndView = mockMvc.perform(post("/grades").contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.0")
                        .param("gradeType", "math")
                        .param("studentId", "1"))
                .andExpect(status().isOk()).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"studentInformation");
        assertEquals(2, service.getStudentInformation(1).getStudentGrades().getMathGradeResults().size());
    }


    @Test
    public void createGradeWithinvalidStudentId() throws  Exception{

        ModelAndView modelAndView = mockMvc.perform(post("/grades").contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.0")
                        .param("gradeType", "math")
                        .param("studentId", "0"))
                .andExpect(status().isOk()).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }


    @Test
    public void createGradeWithInvalidSubject() throws  Exception{

        ModelAndView modelAndView = mockMvc.perform(post("/grades").contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.0")
                        .param("gradeType", "English")
                        .param("studentId", "1"))
                .andExpect(status().isOk()).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }


    @Test
    public void deleteGradeController() throws  Exception{
        assertTrue(mathGradeDao.findById(1).isPresent());
        ModelAndView modelAndView = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{gradeId}/{gradeType}", 1, "math"))
                .andExpect(status().isOk()).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"studentInformation");
        assertFalse(mathGradeDao.findById(1).isPresent());
    }


    @Test
    public void deleteGradeControllerInvalidGradeId() throws  Exception{
        assertFalse(mathGradeDao.findById(0).isPresent());
        ModelAndView modelAndView = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{gradeId}/{gradeType}", 0, "math"))
                .andExpect(status().isOk()).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");

    }


    @Test
    public void deleteGradeControllerInvalidGradeType() throws  Exception{
        assertTrue(mathGradeDao.findById(1).isPresent());
        ModelAndView modelAndView = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{gradeId}/{gradeType}", 1, "literature"))
                .andExpect(status().isOk()).andReturn().getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");

    }
}
