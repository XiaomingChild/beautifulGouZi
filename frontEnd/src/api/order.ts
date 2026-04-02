import request from '../utils/request';
import type { Order } from '../types';

/**
 * 创建订单
 */
export function createOrderApi(data: any) {
  return request<Order>({
    url: '/orders/create',
    method: 'post',
    data
  });
}

/**
 * 获取场次已售出的座位
 */
export function getSoldSeatsApi(scheduleId: number | string) {
  return request<string[]>({
    url: `/orders/sold-seats/${scheduleId}`,
    method: 'get'
  });
}

/**
 * 获取个人订单列表
 */
export function getUserOrdersApi(userId: number) {
  return request<Order[]>({
    url: `/orders/user/${userId}`,
    method: 'get'
  });
}
