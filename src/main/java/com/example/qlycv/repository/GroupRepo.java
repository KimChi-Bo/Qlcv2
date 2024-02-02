package com.example.qlycv.repository;

import com.example.qlycv.entity.Field;
import com.example.qlycv.entity.Group;
import com.example.qlycv.entity.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepo extends JpaRepository<Group, Integer> {

    @Query(value = "SELECT r FROM Group r ", nativeQuery = false)
    List<Group> getListGroup();

    Group findByName(String name);
    Group findById(int id);

    void deleteById(int id);
    @Query(value = "SELECT g.* FROM group_tbl g join staff s on g.id = s.id_group where s.id =:id  ", nativeQuery = true)
    List<Group> findByStaffId(Integer id);
}
