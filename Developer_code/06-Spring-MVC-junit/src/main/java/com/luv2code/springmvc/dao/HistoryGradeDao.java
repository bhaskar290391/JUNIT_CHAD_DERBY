package com.luv2code.springmvc.dao;

import com.luv2code.springmvc.models.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradeDao extends CrudRepository<HistoryGrade,Integer> {

   Iterable<HistoryGrade> findGradeByStudentId(int studentId);

    void deleteByStudentId(int i);
}
