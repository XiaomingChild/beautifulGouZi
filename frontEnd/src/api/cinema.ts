import request from '../utils/request';
import type { Cinema } from '../types';

/**
 * 获取所有影院
 */
export function getAllCinemasApi() {
  return request<Cinema[]>({
    url: '/cinemas/all',
    method: 'get'
  });
}

/**
 * 获取影院详情
 */
export function getCinemaDetailApi(id: number | string) {
  return request<Cinema>({
    url: `/cinemas/${id}`,
    method: 'get'
  });
}
