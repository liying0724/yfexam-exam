package com.yf.exam.modules.sys.echart.controller;

import com.yf.exam.core.api.ApiRest;
import com.yf.exam.core.api.controller.BaseController;
import com.yf.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.yf.exam.modules.sys.echart.dto.EchartDTO;
import com.yf.exam.modules.sys.echart.service.EchartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"图表"})
@RestController
@RequestMapping("/echart/api")
public class EchartController extends BaseController {

    @Autowired
    private EchartService echartService;

    @ApiOperation(value = "查询图表")
    @RequestMapping(value = "/getChartDate", method = { RequestMethod.POST})
    public ApiRest getChartDate(@RequestBody EchartDTO reqDTO) {
        return super.success(echartService.getChartDate(reqDTO));
    }
}
