import { post } from '@/utils/request'

/**
 * 题库详情
 * @param data
 */
export function getChartDate() {
  return post('/echart/api/getChartDate', {})
}

