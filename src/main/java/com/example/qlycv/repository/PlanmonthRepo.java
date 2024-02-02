package com.example.qlycv.repository;

import java.util.List;

import com.example.qlycv.model.response.PlanMonthRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.qlycv.entity.Planmonth;
import com.example.qlycv.model.response.PlanMonth;

@Repository
public interface PlanmonthRepo extends JpaRepository<Planmonth, Integer>{

	@Query(value = "SELECT\r\n" + "	p.id AS planId,\r\n" + "	u.id AS userId,\r\n"
			+ "	s.staff_name AS nameUser,\r\n" + "	f.FIELD_NAME AS field,\r\n" + "	p.content AS content,\r\n"
			+ "	CONCAT( p.MONTH, \"/\", p.YEAR ) AS monthYear \r\n" + "FROM\r\n" + "	planmonth p\r\n"
			+ "	LEFT JOIN field f ON p.id_field = f.id\r\n" + "	LEFT JOIN USER u ON u.id = p.id_user\r\n"
			+ "	LEFT JOIN staff s ON s.id = u.id_staff \r\n" + "WHERE\r\n" + "	p.status = 0\r\n"
			+ "	AND p.MONTH = :month \r\n" + "	AND p.YEAR = :year\r\n", nativeQuery = true)
	List<PlanMonthRes> getPlaneMonthBByMY(int month, int year);

	@Query(value = "SELECT\r\n" + "	p.id AS planId,\r\n" + "	u.id AS userId,\r\n"
			+ "	s.staff_name AS nameUser,\r\n" + "	f.FIELD_NAME AS field,\r\n" + "	p.content AS content,\r\n"
			+ "	CONCAT( p.MONTH, \"/\", p.YEAR ) AS monthYear \r\n" + "FROM\r\n" + "	planmonth p\r\n"
			+ "	LEFT JOIN field f ON p.id_field = f.id\r\n" + "	LEFT JOIN USER u ON u.id = p.id_user\r\n"
			+ "	LEFT JOIN staff s ON s.id = u.id_staff \r\n" + "WHERE\r\n" + "	p.STATUS = 0 \r\n"
			+ "	AND p.MONTH = :month \r\n" + "	AND p.YEAR = :year \r\n"
			+ "	AND s.id_group = ( SELECT s.id_group FROM USER u LEFT JOIN staff s ON s.id = u.id_staff WHERE u.id = :userId )", nativeQuery = true)
	List<PlanMonthRes> getPlaneMonthCByMY(int month, int year, int userId);

	@Query(value = "SELECT\r\n" + "	p.id AS planId,\r\n" + "	u.id AS userId,\r\n"
			+ "	s.staff_name AS nameUser,\r\n" + "	f.FIELD_NAME AS field,\r\n" + "	p.content AS content,\r\n"
			+ "	CONCAT( p.MONTH, \"/\", p.YEAR ) AS monthYear \r\n" + "FROM\r\n" + "	planmonth p\r\n"
			+ "	LEFT JOIN field f ON p.id_field = f.id\r\n" + "	LEFT JOIN USER u ON u.id = p.id_user\r\n"
			+ "	LEFT JOIN staff s ON s.id = u.id_staff \r\n" + "WHERE\r\n" + "	p.status = 0\r\n"
			+ "	AND p.MONTH = :month \r\n" + "	AND p.YEAR = :year\r\n"
			+ "	AND p.id_user = :userId", nativeQuery = true)
	List<PlanMonthRes> getPlaneMonthDByMY(int month, int year, int userId);

	Planmonth findByIdAndIdUser(Integer id, Integer idUser);
}
