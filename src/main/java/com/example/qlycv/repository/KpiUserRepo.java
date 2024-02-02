package com.example.qlycv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.model.response.UserKPIDTO;

import groovy.lang.Tuple;

@Repository
public interface KpiUserRepo extends JpaRepository<KpiUser, Integer> {
    @Query(value = "SELECT r.* FROM KpiUser r Where r.id_user = :id_user", nativeQuery = true)
    List<KpiUser> findAllByUserId(Integer id_user);

   @Query(value = "select r.id,  k.content as name  FROM KpiUser r  join kpi k on r.id_kpi  = k.id Where r.id_user = :id_user", nativeQuery = true)
    List<UserKPIDTO> findKpiUserByUserId(Integer id_user);
}
