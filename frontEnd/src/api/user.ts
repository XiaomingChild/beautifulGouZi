import request from '../utils/request';

/**
 * 用户登录
 * @param data { account, password }
 */
export function loginApi(data: any) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  });
}

/**
 * 用户注册
 * @param data { account, password, nickname, phone }
 */
export function registerApi(data: any) {
  return request({
    url: '/users/register',
    method: 'post',
    data
  });
}

/**
 * 获取当前用户信息 (预留)
 */
export function getUserInfoApi(id: number) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  });
}

/**
 * 发送短信验证码
 * @param phone 手机号
 */
export function sendSmsApi(phone: string) {
  return request({
    url: '/sms/send',
    method: 'get',
    params: { phone }
  });
}

/**
 * 更新用户信息
 */
export function updateUserInfoApi(data: any) {
  return request({
    url: '/users/update',
    method: 'post',
    data
  });
}

/**
 * 退出登录
 */
export function logoutApi() {
  return request({
    url: '/users/logout',
    method: 'post'
  });
}
