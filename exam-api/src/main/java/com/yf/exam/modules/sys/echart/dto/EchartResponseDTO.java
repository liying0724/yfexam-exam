package com.yf.exam.modules.sys.echart.dto;

import lombok.Data;

import java.util.List;

@Data
public class EchartResponseDTO {

    private String name;

    private Integer value;

    private List<EchartResponseDTO> echartResponseDTOList;
}
