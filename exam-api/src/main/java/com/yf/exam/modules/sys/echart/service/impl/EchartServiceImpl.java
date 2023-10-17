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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        List<EchartResponseDTO> chartPieData = baseMapper.getChartPieDate(reqDTO);
        Map<String, LongSummaryStatistics> groupDataPie = getGroupData(chartPieData);
        if (!Objects.isNull(groupDataPie)) {
            getWeekData(chartPieData, groupDataPie);
        }
        resultJson.put("pieData", chartPieData);
        // 查询折线图
        List<EchartResponseDTO> chartLineData = baseMapper.getChartLineDate(reqDTO);
        Map<String, LongSummaryStatistics> groupDataLine = getGroupData(chartLineData);
        if (!Objects.isNull(groupDataLine)) {
            List<Long> lineData = getLineWeekData(groupDataLine);
            resultJson.put("lineData", lineData);
        }
        // 查询柱状图
        List<EchartResponseDTO> chartBarData = baseMapper.getChartBarDate(reqDTO);
        Map<String, Long> groupBarData = getGroupBarData(chartBarData);
        groupBarData = groupBarData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s1, s2) -> s1, LinkedHashMap::new));
        if (!Objects.isNull(groupBarData)) {
            List<Long> barData = new ArrayList<>();
            for (Map.Entry<String, Long> entry: groupBarData.entrySet()
                 ) {
                barData.add(entry.getValue());
            }
            resultJson.put("barData", barData);
        }
        int count = baseMapper.getCourseCount(reqDTO);
        resultJson.put("paperCount", count);
        return resultJson;
    }

    private Map<String, Long> getGroupBarData(List<EchartResponseDTO> chartBarData) {
        if (CollectionUtils.isEmpty(chartBarData)) {
            return null;
        }
        Map<String, Long> collectMap = new HashMap<>();
        for (EchartResponseDTO item: chartBarData
        ) {
            String startTime = DateUtils.formatDate(DateUtils.getMonthStart(), DateUtils.PATTERN_DATE);
            int diffDays = DateUtils.calcDiffDays(item.getCreateDate(), startTime);
            item.setName("第" + (diffDays / 7 + 1) + "周");
            if (item.getValue() > collectMap.getOrDefault(item.getName(), 0L)) {
                collectMap.put(item.getName(), item.getValue());
            }
        }
        return collectMap;
    }

    private List<Long> getLineWeekData(Map<String, LongSummaryStatistics> groupDataLine) {
        List<Long> lineData = new ArrayList<>();
        groupDataLine = groupDataLine.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s1, s2) -> s1, LinkedHashMap::new));
        for (Map.Entry<String, LongSummaryStatistics> entry: groupDataLine.entrySet()
        ) {
            lineData.add(entry.getValue().getSum());
        }
        return lineData;
    }

    private Map<String, LongSummaryStatistics> getGroupData(List<EchartResponseDTO> chartPieDate) {
        if (CollectionUtils.isEmpty(chartPieDate)) {
            return null;
        }
        for (EchartResponseDTO item: chartPieDate
             ) {
            String startTime = DateUtils.formatDate(DateUtils.getMonthStart(), DateUtils.PATTERN_DATE);
            int diffDays = DateUtils.calcDiffDays(item.getCreateDate(), startTime);
            item.setName("第" + (diffDays / 7 + 1) + "周");
        }

        Map<String, LongSummaryStatistics> collectMap = chartPieDate.stream()
                .collect(Collectors.groupingBy(EchartResponseDTO::getName, Collectors.summarizingLong(EchartResponseDTO::getValue)));
        return collectMap;
    }

    private void getWeekData(List<EchartResponseDTO> chartDate, Map<String, LongSummaryStatistics> collectMap) {
        chartDate.clear();
        for (Map.Entry<String, LongSummaryStatistics> entry: collectMap.entrySet()
        ) {
            EchartResponseDTO echartResponseDTO = new EchartResponseDTO();
            String key = entry.getKey();
            long sum = entry.getValue().getSum();
            echartResponseDTO.setName(key);
            echartResponseDTO.setValue(sum);
            chartDate.add(echartResponseDTO);
        }
    }
}
