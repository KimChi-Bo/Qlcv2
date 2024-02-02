package com.example.qlycv.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "kpiuser")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KpiUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_kpi")
    private int idKpi;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "weight")
    private float weight;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "hours_complete")
    private float hoursComplete;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creat_at")
    private Date creatAt;

    @Column(name = "score")
    private float score;

    @Column(name = "rating_score")
    private float ratingScore;
}
