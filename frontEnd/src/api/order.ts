import request from '../utils/request';
import type { Order, BookedSeat } from '../types';

/**
 * 创建订单
 */
export function createOrderApi(data: {
  userId: number;
  scheduleId: number;
  seatInfo: string;
  totalPrice: number;
}) {
  return request<Order>({
    url: '/orders/create',
    method: 'post',
    data
  });
}

/**
 * 支付订单
 */
export function payOrderApi(orderNo: string) {
  return request<string>({
    url: `/orders/pay/${orderNo}`,
    method: 'post'
  });
}

/**
 * 获取场次已锁定/售出的座位
 */
export function getBookedSeatsApi(scheduleId: number | string) {
  return request<BookedSeat[]>({
    url: `/orders/booked-seats/${scheduleId}`,
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

/**
 * 获取订单详情
 */
export function getOrderDetailApi(orderNo: string) {
  return request<Order>({
    url: `/orders/detail/${orderNo}`,
    method: 'get'
  });
}
