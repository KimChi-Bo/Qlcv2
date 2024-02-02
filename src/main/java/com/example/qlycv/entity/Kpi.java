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
@Table(name = "kpi")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Kpi {

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @Column(name = "id_field")
    private int idField;
    
    @Column(name = "content")
    private String content;
    
    @Column(name = "weight")
    private float weight;
    
    @Column(name = "month")
    private int month;
    
    @Column(name = "year")
    private int year;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "id_group")
    private int idGroup;
}
