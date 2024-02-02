package com.example.qlycv.repository;

import java.util.Date;
import java.util.List;

import com.example.qlycv.entity.KpiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.qlycv.entity.Kpi;

@Repository
public interface KpiRepo extends JpaRepository<Kpi, Integer> {
    @Query(value = "SELECT r FROM Kpi r ", nativeQuery = false)
    List<Kpi> findByUserName(String userName);

    @Query("SELECT ku FROM KpiUser ku WHERE ku.idUser = :idUser AND ku.idKpi = :idKpi")
    KpiUser findByIdUserAndIdKpi(int idUser, int idKpi);

    @Modifying
    @Query("UPDATE KpiUser ku SET ku.weight = :weight WHERE ku.idUser = :idUser AND ku.idKpi = :idKpi")
    void updateWeight(@Param("idUser") int idUser, @Param("idKpi") int idKpi, @Param("weight") float weight);


}
