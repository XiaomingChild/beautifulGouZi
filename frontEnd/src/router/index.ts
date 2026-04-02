import { createRouter, createWebHistory } from 'vue-router'
import type { RouteLocationNormalized } from 'vue-router'

import HomeView from '../views/home.vue'
import LoginView from '../views/login.vue'
import RegisterView from '../views/register.vue'
import MovieDetailView from '../views/movieDetail.vue'
import SeatSelectionView from '../views/seatSelection.vue'
import ProfileView from '../views/profile/index.vue'
import AvatarSelectView from '../views/profile/AvatarSelect.vue'
import MoviesView from '../views/movies.vue'
import CinemasView from '../views/cinemas.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { title: '首页', requiresAuth: false }
    },
    {
      path: '/movies',
      name: 'movies',
      component: MoviesView,
      meta: { title: '电影列表', requiresAuth: false }
    },
    {
      path: '/cinemas',
      name: 'cinemas',
      component: CinemasView,
      meta: { title: '影院列表', requiresAuth: false }
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
      path: '/movieDetail/:id',
      name: 'movieDetail',
      component: MovieDetailView,
      meta: { title: '电影详情', requiresAuth: false },
    },
    {
      path: '/seatSelection',
      name: 'seatSelection',
      component: SeatSelectionView,
      meta: { title: '选座购票', requiresAuth: true },
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { title: '个人中心', requiresAuth: true },
    },
    {
      path: '/avatar-select',
      name: 'avatarSelect',
      component: AvatarSelectView,
      meta: { title: '更换头像', requiresAuth: true },
    }
  ],
})

// 路由前置守卫
router.beforeEach((to: RouteLocationNormalized) => {
  const requiresAuth = to.meta.requiresAuth === true
  const token = localStorage.getItem('token')

  if (requiresAuth && !token) {
    return { path: '/login' }
  }
  return true
})

export default router
