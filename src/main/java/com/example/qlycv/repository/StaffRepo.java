package com.example.qlycv.repository;

import com.example.qlycv.entity.Staff;
import com.example.qlycv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepo extends JpaRepository<Staff, Integer> {
    Staff findById(int id);

    @Query(value = "SELECT f FROM Staff f", nativeQuery = false)
    List<Staff> getListStaff();

    void deleteById(int id);
    List<Staff> findByidGroup(int idGroup);
}
