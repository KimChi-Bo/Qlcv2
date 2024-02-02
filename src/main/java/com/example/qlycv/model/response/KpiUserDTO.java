package com.example.qlycv.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Data
@AllArgsConstructor
public class KpiUserDTO {
    private Integer kpiId;
    private String Name;
}
