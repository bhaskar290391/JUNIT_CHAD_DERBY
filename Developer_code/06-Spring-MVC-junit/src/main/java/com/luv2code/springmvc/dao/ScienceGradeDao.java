package com.luv2code.springmvc.dao;

import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradeDao extends CrudRepository<ScienceGrade,Integer> {

    Iterable<ScienceGrade> findGradeByStudentId(int studentId);

    void deleteByStudentId(int i);
}