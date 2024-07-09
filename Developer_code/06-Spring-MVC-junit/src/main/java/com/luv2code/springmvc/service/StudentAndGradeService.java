package com.luv2code.springmvc.service;


import com.luv2code.springmvc.dao.HistoryGradeDao;
import com.luv2code.springmvc.dao.MathGradeDao;
import com.luv2code.springmvc.dao.ScienceGradeDao;
import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao collegeStudentDao;

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

    @Autowired
    private StudentGrades grades;

    public void createStudent(String firstName,String lastName,String email){
        CollegeStudent student=new CollegeStudent("bhaskar","mudaliyar","bhaksar.mudaliyar@gmail.com");
        student.setId(0);
        collegeStudentDao.save(student);
    }

    public boolean checkStudentIfNull(int id) {

        Optional<CollegeStudent> byId = collegeStudentDao.findById(id);

        if(byId.isPresent()){
            return true;
        }

        return  false;
    }

    public void deleteStudent(int i) {
       if( checkStudentIfNull(i)){
           collegeStudentDao.deleteById(i);
           mathGradeDao.deleteByStudentId(i);
           scienceGradeDao.deleteByStudentId(i);
           historyGradeDao.deleteByStudentId(i);
       }
    }

    public Iterable<CollegeStudent> getGradebooks() {

       return collegeStudentDao.findAll();
    }

    public boolean createGrade(Double grade, int studentId, String gradeType) {

        if(!checkStudentIfNull(studentId)){
            return  false;
        }

        if(grade >0 && grade<=100.0){
            
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setStudentId(studentId);
                mathGrade.setGrade(grade);
                mathGradeDao.save(mathGrade);
                return  true;

            } else if (gradeType.equals("science")) {
                scienceGrade.setId(0);
                scienceGrade.setStudentId(studentId);
                scienceGrade.setGrade(grade);
                scienceGradeDao.save(scienceGrade);
                return  true;
            }else if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setStudentId(studentId);
                historyGrade.setGrade(grade);
                historyGradeDao.save(historyGrade);
                return  true;
            }
        }

        return  false;

    }

    public int deletGrade(int id, String gradeType) {
        int studentId=0;

        if(gradeType.equals("math")){
            Optional<MathGrade> student = mathGradeDao.findById(id);
            if(!student.isPresent()){
                return  studentId;
            }

            studentId=student.get().getStudentId();
            mathGradeDao.deleteById(studentId);
        }

        if(gradeType.equals("science")){
            Optional<ScienceGrade> student = scienceGradeDao.findById(id);
            if(!student.isPresent()){
                return  studentId;
            }

            studentId=student.get().getStudentId();
            scienceGradeDao.deleteById(studentId);
        }


        if(gradeType.equals("history")){
            Optional<HistoryGrade> student = historyGradeDao.findById(id);
            if(!student.isPresent()){
                return  studentId;
            }

            studentId=student.get().getStudentId();
            historyGradeDao.deleteById(studentId);
        }

        return  studentId;
    }

    public GradebookCollegeStudent getStudentInformation(int studentId) {

        if(!checkStudentIfNull(studentId)){
            return null;
        }

        Optional<CollegeStudent> student = collegeStudentDao.findById(studentId);

        Iterable<MathGrade> mathGradeStudent = mathGradeDao.findGradeByStudentId(studentId);

        Iterable<ScienceGrade> scienceGradeStudent = scienceGradeDao.findGradeByStudentId(studentId);

        Iterable<HistoryGrade> historyGradeStudent = historyGradeDao.findGradeByStudentId(studentId);

        List<Grade> mathData=new ArrayList<>();
        mathGradeStudent.forEach(mathData::add);

        List<Grade> scienceData=new ArrayList<>();
        scienceGradeStudent.forEach(scienceData::add);

        List<Grade> historyData=new ArrayList<>();
        historyGradeStudent.forEach(historyData::add);

        grades.setMathGradeResults(mathData);
        grades.setScienceGradeResults(scienceData);
        grades.setHistoryGradeResults(historyData);

        return new GradebookCollegeStudent(student.get().getId(),student.get().getFirstname(),student.get().getLastname(),
                student.get().getEmailAddress(),grades);

    }


    public void getStudentInformationData(int id, Model m) {
        GradebookCollegeStudent studentInformation = getStudentInformation(id);

        if(studentInformation.getStudentGrades().getMathGradeResults().size() >0){
            m.addAttribute("mathAverage",studentInformation.getStudentGrades().findGradePointAverage(
                    studentInformation.getStudentGrades().getMathGradeResults()
            ));
        }else{
            m.addAttribute("mathAverage","N/A");
        }

        if(studentInformation.getStudentGrades().getScienceGradeResults().size() >0){
            m.addAttribute("scienceAverage",studentInformation.getStudentGrades().findGradePointAverage(
                    studentInformation.getStudentGrades().getScienceGradeResults()
            ));
        }else{
            m.addAttribute("scienceAverage","N/A");
        }


        if(studentInformation.getStudentGrades().getHistoryGradeResults().size() >0){
            m.addAttribute("historyAverage",studentInformation.getStudentGrades().findGradePointAverage(
                    studentInformation.getStudentGrades().getHistoryGradeResults()
            ));
        }else{
            m.addAttribute("historyAverage","N/A");
        }
    }
}
