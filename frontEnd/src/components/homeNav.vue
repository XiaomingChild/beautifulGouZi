<template>
  <header class="nav">
    <div class="nav-inner">
      <div class="brand" @click="goHome">
        <img class="brand-logo" :src="logoUrl" alt="天堂电影 Logo" />
        <span class="brand-name">天堂电影</span>
      </div>
      <nav class="menu">
        <RouterLink to="/home" class="menu-link" active-class="is-active">首页</RouterLink>
        <RouterLink to="/home" class="menu-link">电影</RouterLink>
        <RouterLink to="/home" class="menu-link">影院</RouterLink>
        <RouterLink to="/home" class="menu-link">我的</RouterLink>
      </nav>
      <div class="actions">
        <button class="btn ghost" type="button" @click="goLogin">登录</button>
        <button class="btn solid" type="button" @click="goRegister">注册</button>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import logoUrl from '../assets/logo.svg';

const router = useRouter();

const goHome = () => router.push('/home');
const goLogin = () => router.push('/login');
const goRegister = () => router.push('/register');
</script>

<style scoped lang="scss">
.nav {
  background: #ffffff;
  border-bottom: 1px solid #f1f3f6;
  position: sticky;
  top: 0;
  z-index: 10;
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 68px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  gap: 24px;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.brand-logo {
  width: 32px;
  height: 32px;
}

.brand-name {
  font-size: 20px;
  font-weight: 700;
  color: #1f2f3f;
}

.menu {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  gap: 0;
}

.menu-link {
  color: #4b5563;
  font-size: 14px;
  text-decoration: none;
  position: relative;
  padding-bottom: 4px;
}

.menu-link:hover,
.menu-link.is-active {
  color: #15b8a6;
}

.menu-link.is-active::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 3px;
  border-radius: 3px;
  background: #15b8a6;
}

.actions {
  display: inline-flex;
  gap: 12px;
  margin-left: 32px;
}

.btn {
  min-width: 72px;
  height: 36px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.15s ease;
}

.btn.ghost {
  border-color: #15b8a6;
  color: #15b8a6;
  background: #ffffff;
}

.btn.ghost:hover {
  background: #e9f7f9;
}

.btn.solid {
  background: #15b8a6;
  color: #ffffff;
}

.btn.solid:hover {
  background: #129c90;
}
</style>
