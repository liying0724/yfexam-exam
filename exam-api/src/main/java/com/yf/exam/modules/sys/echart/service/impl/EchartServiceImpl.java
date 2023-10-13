package com.yf.exam.modules.sys.echart.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.core.utils.DateUtils;
import com.yf.exam.core.utils.StringUtils;
import com.yf.exam.modules.sys.depart.entity.SysDepart;
import com.yf.exam.modules.sys.echart.dto.EchartDTO;
import com.yf.exam.modules.sys.echart.dto.EchartResponseDTO;
import com.yf.exam.modules.sys.echart.mapper.EchartMapper;
import com.yf.exam.modules.sys.echart.service.EchartService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EchartServiceImpl extends ServiceImpl<EchartMapper, SysDepart> implements EchartService {

    @Override
    public JSONObject getChartDate(EchartDTO reqDTO) {
        JSONObject resultJson = new JSONObject();

        if (StringUtils.isBlank(reqDTO.getStartDate())) {
            reqDTO.setStartDate(DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYY_MM) + "-01");
        }
        if (StringUtils.isBlank(reqDTO.getEndDate())) {
            reqDTO.setEndDate(DateUtils.formatDate(new Date(), DateUtils.PATTERN_DATE));

        }
        // 查询饼状图
        List<EchartResponseDTO> chartPieDate = baseMapper.getChartPieDate(reqDTO);
        resultJson.put("pieDate", chartPieDate);
        // 查询折线图
        List<EchartResponseDTO> chartLineDate = baseMapper.getChartLineDate(reqDTO);
        resultJson.put("lineDate", chartLineDate);
        return resultJson;
    }
}
