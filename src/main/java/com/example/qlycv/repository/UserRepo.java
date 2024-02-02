package com.example.qlycv.repository;

import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.qlycv.entity.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	User findByUserName(String userName);
	
	User findByUserNameAndIdStaff(String userName, int idStaff);

	@Query(value = "SELECT f FROM User f", nativeQuery = false)
	List<User> getListUser();

	void deleteById(int id);

	User findByIdStaff(int idStaff);

	User findById(int id);

	@Query("SELECT s FROM Staff s " +
			"LEFT JOIN User u ON s.id = u.idStaff " +
			"WHERE s.idGroup = (SELECT st.idGroup FROM Staff st WHERE st.id = u.idStaff) " +
			"AND u.role LIKE '%D%'")
	List<Staff> findStaffByUsername(String username);

	@Query("SELECT k FROM KpiUser k " +
			"WHERE  k.idKpi=:idKpi " +
			"AND k.idUser=:idUser")
	KpiUser kpiUserbyStaffIdAndIdKpi(int idUser, int idKpi);

	@Query("SELECT u.id FROM User u " +
			"WHERE  u.idStaff=:staffId ")
	int findByIdstaff(int staffId);
}
