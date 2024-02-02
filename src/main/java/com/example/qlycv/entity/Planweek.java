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
@Table(name = "planweek")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Planweek {

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(name = "id_kpi_user")
	private int idKpiUser;
	
	@Column(name = "content")
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_at")
	private Date startAt;
	
	@Column(name = "hours_complete")
	private float hoursComplete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;
	
	@Column(name = "status")
	private int status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delete_at")
	private Date deleteAt;

	
	@Column(name = "id_user")
	private int idUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "date")
	private Date planeDate;

	@Column(name = "content_app")
	private String contentApp;

	@Column(name = "id_group")
	private int idGroup;
}
