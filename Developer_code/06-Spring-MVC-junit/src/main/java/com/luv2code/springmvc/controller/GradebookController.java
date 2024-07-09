package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

    @Autowired
    private Gradebook gradebook;

    @Autowired
    private StudentAndGradeService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model m) {
        Iterable<CollegeStudent> gradebooks = service.getGradebooks();
        m.addAttribute(gradebooks);
        return "index";
    }


    @PostMapping(value = "/")
    public String CreateStudents(@ModelAttribute("students") CollegeStudent students,Model m) {
        service.createStudent(students.getFirstname(),students.getLastname(),students.getEmailAddress());
        Iterable<CollegeStudent> gradebooks = service.getGradebooks();
        m.addAttribute("students",gradebooks);
        return "index";
    }


    @GetMapping("/delete/student/{id}")
    public String deleteStudents(@PathVariable int id, Model m) {
        if(!service.checkStudentIfNull(id)){
            return  "error";
        }

        service.deleteStudent(id);
        Iterable<CollegeStudent> gradebooks = service.getGradebooks();
        m.addAttribute("students",gradebooks);
        return "index";
    }


    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {

        if(!service.checkStudentIfNull(id)){
            return  "error";
        }

        service.getStudentInformationData(id, m);
        return "studentInformation";
    }


    @PostMapping("/grades")
    public String createGrades(@RequestParam("grade") Double grade,
            @RequestParam("gradeType") String gradeType,
                               @RequestParam("studentId") int studentId,Model m) {

        if(!service.checkStudentIfNull(studentId)){
            return  "error";
        }

        boolean success=service.createGrade(grade,studentId,gradeType);
        if(!success){
            return  "error";
        }
        service.getStudentInformationData(studentId, m);
        return "studentInformation";
    }



    @GetMapping("/grades/{gradeId}/{gradeType}")
    public String deleteGrade(@PathVariable("gradeId") int gradeId,
                              @PathVariable("gradeType") String gradeType,
                              Model m){

        int studentId=service.deletGrade(gradeId,gradeType);

        if(studentId ==0){
            return  "error";
        }

        service.getStudentInformationData(studentId,m);
        return  "studentInformation";
    }

}
