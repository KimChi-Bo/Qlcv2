package com.example.qlycv.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class KpiUserDetails {
    private int idKpi;
    private int stt;
    private String Kpi;
    private float weight;
    private List<KpiUser> staffWeigh;

}
