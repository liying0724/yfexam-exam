package com.yf.exam.modules.sys.echart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.exam.modules.sys.depart.entity.SysDepart;
import com.yf.exam.modules.sys.echart.dto.EchartDTO;
import com.yf.exam.modules.sys.echart.dto.EchartResponseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EchartMapper  extends BaseMapper<SysDepart> {

    List<EchartResponseDTO> getChartPieDate(@Param("reqDTO") EchartDTO reqDTO);

    List<EchartResponseDTO> getChartLineDate(EchartDTO reqDTO);
}
