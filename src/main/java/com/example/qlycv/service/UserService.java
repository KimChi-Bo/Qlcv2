package com.example.qlycv.service;

import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.entity.Role;
import com.example.qlycv.entity.Staff;
import com.example.qlycv.repository.RoleRepo;
import com.example.qlycv.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.entity.User;
import com.example.qlycv.form.VP001Form;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.repository.UserRepo;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private StaffRepo staffRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public boolean checkLogin(VP001Form form) {

		User user = userRepo.findByUserName(form.getUserName());

		if (ObjectUtils.isEmpty(user)) {
			return false;
		}

		if (!passwordEncoder().matches(form.getPass(), user.getPass())) {
			return false;
		}

		return true;
	}

	public boolean registerAccount(VP002Form form) {


		if (ObjectUtils.isEmpty(form.getIdStaff()) || ObjectUtils.isEmpty(form.getUserName())
				|| ObjectUtils.isEmpty(form.getPass()) || ObjectUtils.isEmpty(form.getRole())) {
			return false;
		}
		User user = new User();
		String role = form.getRole();
		if (!ObjectUtils.isEmpty(userRepo.findByUserName(form.getUserName()))){
			user = userRepo.findByUserName(form.getUserName());
			if(user.getRole().equals(form.getRole())){
				return false;
			}
			role = user.getRole() + form.getRole();
			user.setRole(role);
		}

		user.setIdStaff(Integer.parseInt(form.getIdStaff()));
		user.setUserName(form.getUserName());
		user.setPass(passwordEncoder().encode(form.getPass()));
		user.setRole(role);

		userRepo.save(user);
		
		return true;
	}
	
	public boolean changeAccount(VP002Form form) {
		User user = userRepo.findByUserNameAndIdStaff(form.getUserName(), Integer.parseInt(form.getIdStaff()));

		if (ObjectUtils.isEmpty(user)) {
			return false;
		}

		user.setIdStaff(Integer.parseInt(form.getIdStaff()));
		user.setUserName(form.getUserName());
		user.setPass(passwordEncoder().encode(form.getPass()));
		user.setRole(form.getRole());

		userRepo.save(user);
		
		return true;
	}

	public List<Role> getRoleName(){
		List<Role> roleList = roleRepo.getListRole();
		return roleList;
	}

	public List<User> getListUser(){
		List<User> userList = userRepo.getListUser();
		return userList;
	}


	public Staff getStaffbyId(int id){
		return staffRepo.findById(id);
	}

	public boolean removeUser(int id){
		userRepo.deleteById(id);
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username);

		return new CustomUserDetails(user);
	}

	public List<Staff> getStaffsByGroupId(int groupId)	{
		return staffRepo.findByidGroup(groupId);
	}

	public User getUserByStaffId(int staffId) {
		return userRepo.findByIdStaff(staffId);
	}

	public Staff getStaffbyUserId(int userId) {
		return staffRepo.findById(userRepo.findById(userId).getIdStaff());
	}
	public int getUserIdByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username);

		return user.getId();
	}

	public List<Staff> getListUserStaffByUsername(String username) {
		List<Staff> list = new ArrayList<>();
		return userRepo.findStaffByUsername(username);
	}

	public KpiUser getKpiUserByStaffIdAndIdKpi(int idUser, int idKpi) {
		return userRepo.kpiUserbyStaffIdAndIdKpi(idUser, idKpi);
	}

	public int getUserIdByStaffId(int staffId) {
		return userRepo.findByIdstaff(staffId);
	}


}
