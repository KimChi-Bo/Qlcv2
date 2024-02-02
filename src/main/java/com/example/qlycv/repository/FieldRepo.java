package com.example.qlycv.repository;

import com.example.qlycv.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepo extends JpaRepository<Field, Integer> {
    Field  findByName(String name);
    Field findById(int id);

    void deleteById(int id);

    @Query(value = "SELECT f FROM Field f", nativeQuery = false)
    List<Field> getListField();

}
