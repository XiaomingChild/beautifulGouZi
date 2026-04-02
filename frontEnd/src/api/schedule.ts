import request from '../utils/request';
import type { Schedule } from '../types';

/**
 * 获取场次详情
 */
export function getScheduleDetailApi(id: number | string) {
  return request<Schedule>({
    url: `/schedules/${id}`,
    method: 'get'
  });
}
