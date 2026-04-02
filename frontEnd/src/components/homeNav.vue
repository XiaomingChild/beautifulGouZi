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
        <RouterLink to="/movies" class="menu-link" active-class="is-active">电影</RouterLink>
        <RouterLink to="/cinemas" class="menu-link" active-class="is-active">影院</RouterLink>
      </nav>
      <div class="actions">
        <template v-if="!user.account">
          <button class="btn ghost" type="button" @click="goLogin">登录</button>
          <button class="btn solid" type="button" @click="goRegister">注册</button>
        </template>
        <div v-else class="user-chip" ref="chipRef">
          <img class="user-avatar" :src="user.avatar" alt="用户头像" @click.stop="goProfile('profile')" />
          <span class="user-name" @click.stop="toggleMenu">{{ user.nickname || user.account }}</span>
          <svg class="caret" viewBox="0 0 24 24" aria-hidden="true" @click.stop="toggleMenu">
            <path d="M7 9l5 5 5-5H7z" fill="currentColor" />
          </svg>
          <div v-show="menuOpen" class="dropdown">
            <button
              v-for="item in menuItems"
              :key="item.tab"
              type="button"
              class="dropdown-item"
              @click.stop="goProfile(item.tab)"
            >
              <span class="dropdown-text">{{ item.label }}</span>
            </button>
            <hr class="dropdown-divider" />
            <button
              type="button"
              class="dropdown-item logout-item"
              @click.stop="handleLogout"
            >
              <span class="dropdown-text">退出登录</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import logoUrl from '../assets/logo.svg';
import { usePosStore } from "../store/position";
import { useUserStore } from "../store/userInfo";

const posStore = usePosStore();
const { position } = storeToRefs(posStore);
const userStore = useUserStore();
const { state: user } = storeToRefs(userStore);
const router = useRouter();

const menuOpen = ref(false);
const chipRef = ref<HTMLElement | null>(null);
const menuItems = [
  { tab: 'profile', label: '基本设置' },
  { tab: 'records', label: '我的订单' },
  { tab: 'favorites', label: '我的收藏' },
  { tab: 'security', label: '账号安全' },
];

const goHome = () => router.push('/home');
const goLogin = () => router.push('/login');
const goRegister = () => router.push('/register');
const goProfile = (tab: string) => {
  router.push({ path: '/profile', query: { tab } });
  menuOpen.value = false;
};
const toggleMenu = () => (menuOpen.value = !menuOpen.value);

const handleLogout = () => {
  ElMessageBox.confirm(
    '确定要退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      localStorage.removeItem('token');
      userStore.resetUser();
      ElMessage.success('已退出登录');
      menuOpen.value = false;
      router.push('/login');
    })
    .catch(() => {
    });
};

const handleClickOutside = (e: MouseEvent) => {
  if (!menuOpen.value) return;
  const el = chipRef.value;
  if (el && !el.contains(e.target as Node)) {
    menuOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});
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
  max-width: 1500px;
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
  font-size: 14px;
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
  margin-left: 100px;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 150px;
}

.menu-link {
  color: #4b5563;
  font-size: 15px;
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
  margin-left: auto;
}

.user-chip {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 5px 12px;
  border-radius: 999px;
  background: linear-gradient(135deg, #f7fafc, #eef2ff);
  color: #0f172a;
  font-weight: 700;
  border: 1px solid #e5e7eb;
  box-shadow: 0 0 0 rgba(15, 23, 42, 0);
  cursor: pointer;
  transition: all 0.18s ease;
}

.user-avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #d8dee9;
}

.user-name {
  line-height: 1;
  letter-spacing: 0.2px;
}

.btn {
  min-width: 72px;
  height: 36px;
  border-radius: 6px;
  font-size: 15px;
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

.caret {
  width: 14px;
  height: 14px;
  color: #94a3b8;
}

.dropdown {
  position: absolute;
  top: 115%;
  right: 0;
  min-width: 200px;
  background: linear-gradient(180deg, #ffffff, #f8fafc);
  border: 1px solid #e2e8f0;
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.18);
  border-radius: 14px;
  padding: 12px;
  display: grid;
  gap: 6px;
  z-index: 20;
}

.dropdown-item {
  text-align: left;
  border: 0;
  background: transparent;
  padding: 10px 12px;
  border-radius: 12px;
  color: #0f172a;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.14s ease;
  display: flex;
  align-items: center;
  gap: 10px;
}

.dropdown-item:hover {
  background: #e8f5ff;
  color: #0b6bcb;
}

.dropdown-text {
  flex: 1;
}

.dropdown-divider {
  border: 0;
  border-top: 1px solid #e2e8f0;
  margin: 4px 0;
}

.logout-item {
  color: #ef4444 !important;
}

.logout-item:hover {
  background: #fef2f2 !important;
  color: #dc2626 !important;
}

.user-chip:hover {
  box-shadow: 0 10px 26px rgba(15, 23, 42, 0.16);
}
</style>
