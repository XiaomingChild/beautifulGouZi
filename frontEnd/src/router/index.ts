import { createRouter, createWebHistory } from 'vue-router'
import type { RouteLocationNormalized } from 'vue-router'

import HomeView from '../views/home.vue'
import LoginView from '../views/login.vue'
import RegisterView from '../views/register.vue'
import HomeMovieView from '../views/homeMovie.vue'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {//默认路由为home
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home-alias',
      component: HomeView,
      meta: { title: '首页', requiresAuth: false }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { title: '登录', requiresAuth: false },
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { title: '注册', requiresAuth: false },
    },
    {
      path: '/homeMovie',
      name: 'homeMovie',
      component: HomeMovieView,
      meta: { title: '电影详情', requiresAuth: true },
    }
  ],
})
//路由前置守卫
router.beforeEach((to: RouteLocationNormalized) => {
  //判断是否拦截
  const requiresAuth = to.meta.requiresAuth !== false
  const token = localStorage.getItem('token')

  if (!requiresAuth) {
    return true
  }

  if (token) {
    return true
  }

  return {
    path: '/login'
  }
})

export default router
