import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8080/api', // 你的 SpringBoot 后端地址
  timeout: 5000, // 请求超时时间
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 可以在这里统一添加 Token
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
    // 直接返回数据主体
    return response.data;
  },
  (error) => {
    console.error('API Error:', error);
    ElMessage.error(error.response?.data || '网络请求错误');
    return Promise.reject(error);
  }
);

export default service;
