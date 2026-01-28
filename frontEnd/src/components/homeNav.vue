<template>
  <header class="nav">
    <div class="nav-inner">
     
      <div class="brand" @click="goHome">
        <img class="brand-logo" :src="logoUrl" alt="天堂电影 Logo" />
        <span class="brand-name">天堂电影</span>
      </div>
       <div class="location-block">
        <svg class="loc-icon" viewBox="0 0 24 24" aria-hidden="true">
          <path
            d="M12 2.75a6.25 6.25 0 0 0-6.25 6.25c0 4.35 5.19 9.54 5.41 9.76.46.44 1.22.44 1.68 0 .22-.22 5.41-5.41 5.41-9.76A6.25 6.25 0 0 0 12 2.75Zm0 8.5a2.25 2.25 0 1 1 0-4.5 2.25 2.25 0 0 1 0 4.5Z"
            fill="currentColor"
          />
        </svg>
        <span class="loc-text">{{ position }}</span>
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
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';
import logoUrl from '../assets/logo.svg';
import { usePosStore } from "../store/position";
const posStore = usePosStore();
const { position } = storeToRefs(posStore);
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

.location-block {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #0aaabf;
  font-weight: 600;
  font-size: 13px;
  padding: 6px 10px;
  border-radius: 999px;
  background: #e9f7f9;
}

.loc-icon {
  width: 16px;
  height: 16px;
}

.loc-text {
  line-height: 1;
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
