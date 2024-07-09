package com.luv2code.springmvc.dao;

import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradeDao extends CrudRepository<MathGrade,Integer> {

    Iterable<MathGrade> findGradeByStudentId(int studentId);

    void deleteByStudentId(int i);
}