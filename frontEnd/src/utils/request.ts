import axios from 'axios';
import { ElMessage } from 'element-plus';

/**
 * 统一后端返回结构
 */
export interface Result<T = any> {
  code: number;
  message: string;
  data: T;
}

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data as Result;
    
    // 如果 code 不是 200，说明业务请求失败
    if (res.code !== 200) {
      ElMessage.error(res.message || '业务请求失败');
      return Promise.reject(new Error(res.message || 'Error'));
    }
    
    // 返回 Result 中的 data
    return res.data;
  },
  (error) => {
    console.error('API Error:', error);
    const message = error.response?.data?.message || error.message || '网络请求错误';
    ElMessage.error(message);
    return Promise.reject(error);
  }
);

export default service;
