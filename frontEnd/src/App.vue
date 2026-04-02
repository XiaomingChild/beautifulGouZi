<template>
  <div class="app-container">
    <HomeNav />
    
    <!-- 面包屑导航：仅在非首页展示 -->
    <nav v-if="route.path !== '/home'" class="breadcrumb-nav">
      <div class="content-wrapper">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="route.meta.title">{{ route.meta.title }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </nav>

    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import HomeNav from './components/homeNav.vue'

const route = useRoute()
</script>

<style lang="scss">
body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background: #f7f8fb;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  .breadcrumb-nav {
    background: #fff;
    padding: 12px 0;
    border-bottom: 1px solid #f1f3f6;
    margin-bottom: 2px;

    .content-wrapper {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 20px;
    }

    :deep(.el-breadcrumb__inner) {
      font-size: 14px;
      color: #94a3b8;
      &.is-link:hover { color: #15b8a6; }
    }
    
    :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
      color: #1f2937;
      font-weight: 600;
    }
  }

  .main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
}
</style>
