package com.example.qlycv.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Report {

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(name = "id_plan_month")
	private int idPlanMonth;
	
	@Column(name = "id_user")
	private int idUser;
	
	@Column(name = "content")
	private String Content;
	
	@Column(name = "hours_complete")
	private float hoursComplete;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@Column(name = "status")
	private int status;

	@Column(name = "note")
	private String note;
}
