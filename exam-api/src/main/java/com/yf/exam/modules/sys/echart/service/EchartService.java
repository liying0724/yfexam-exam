package com.yf.exam.modules.sys.echart.service;

import com.alibaba.fastjson.JSONObject;
import com.yf.exam.modules.sys.echart.dto.EchartDTO;

public interface EchartService {

    JSONObject getChartDate(EchartDTO reqDTO);
}
