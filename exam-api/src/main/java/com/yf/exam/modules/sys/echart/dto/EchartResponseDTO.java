package com.yf.exam.modules.sys.echart.dto;

import lombok.Data;

import java.util.List;

@Data
public class EchartResponseDTO {

    private String createDate;

    private Long value;

    private String name;

    private List<EchartResponseDTO> echartResponseDTOList;
}
