package com.example.qlycv.repository;

import com.example.qlycv.entity.Planweek;
import com.example.qlycv.entity.Role;
import com.example.qlycv.model.response.UserPlanWeek;

import jakarta.persistence.NamedNativeQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PlanweekRepo extends JpaRepository<Planweek, Integer> {
    @Query(value = "SELECT r FROM Planweek r ", nativeQuery = false)
    Page<Planweek> searchPlanWeek(String keyword, Pageable pageable);

    @Query(value = "select  r.id, k2.content kpiName, r.date as planDate, r.content planContent, r.hours_complete planTime, rp.status, rp.content reportContent, f.field_name as fieldName , r.content_app as approvedContent, rp.note reportNote, rp.hours_complete as reportTime FROM Planweek r join kpiuser k  on r.id_kpi_user  =k.id  "+
            "join kpi k2 on k2.id  = k.id_kpi " +
            "join field f on k2.id_field = f.id " +
            "left join report_tbl rp on r.id = rp.id_plan_month " +
            "where r.id_user  = :user_id " +
            "and r.date BETWEEN :start_date AND :end_date", nativeQuery = true)
    List<UserPlanWeek> findAllByUserId(String start_date, String end_date, Integer user_id);

    @Query(value = "select st.staff_name as staffName, gr.group_name as groupName, u.user_name as userName, r.id, k2.content kpiName, r.date as planDate, r.content planContent, r.hours_complete planTime, rp.status, rp.content reportContent, f.field_name as fieldName , r.content_app as approvedContent, rp.note reportNote, rp.hours_complete as reportTime FROM Planweek r join kpiuser k  on r.id_kpi_user  =k.id  "+
            "join kpi k2 on k2.id  = k.id_kpi " +
            "join field f on k2.id_field = f.id " +
            "join user u on u.id = k.id_user " +
            "join group_tbl gr on gr.id = r.id_group " +
            "join staff st on st.id = u.id_staff " +
            "left join report_tbl rp on r.id = rp.id_plan_month " +
            "where (r.id_group in (:id_group))" +
            "and r.date BETWEEN :start_date AND :end_date", nativeQuery = true)
    List<UserPlanWeek> findAllByGroup(String start_date, String end_date, List<Integer> id_group);
}
