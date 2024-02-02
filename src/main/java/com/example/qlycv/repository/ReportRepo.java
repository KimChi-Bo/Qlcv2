package com.example.qlycv.repository;

import com.example.qlycv.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepo extends JpaRepository<Report, Integer> {
    @Query(value = "SELECT * FROM report_tbl r where id_plan_month =:id_plan_month ", nativeQuery  = true)
    Report findByPlanId(Integer id_plan_month);
}
