<template>
  <div class="cinemas-page">
    <el-backtop :right="60" :bottom="100">
      <div class="back-top-btn">
        <svg viewBox="0 0 1024 1024" width="24" height="24">
          <path fill="currentColor" d="M512 320L192 640h640L512 320z"></path>
        </svg>
      </div>
    </el-backtop>

    <main class="content-wrapper list-container">
      <div class="cinema-list" v-if="cinemas.length > 0">
        <CinemaCard 
          v-for="cinema in cinemas" 
          :key="cinema.id" 
          :cinema="cinema" 
          @go-detail="goCinemaDetail"
        />
      </div>
      <el-empty v-else description="暂无影院数据" />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import CinemaCard from '../components/cinema/CinemaCard.vue';
import { getAllCinemasApi } from '../api/cinema';
import type { Cinema } from '../types';

const router = useRouter();
const cinemas = ref<Cinema[]>([]);

const loadCinemas = async () => {
  try {
    const res = await getAllCinemasApi();
    cinemas.value = res || [];
  } catch (error) {
    console.error('Failed to load cinemas:', error);
  }
};

const goCinemaDetail = (id: number) => {
  // 暂时影院详情页还没做，可以跳转到对应的影院详情
  // 或者如果直接在购票流中，可以跳转到电影列表过滤该影院
  console.log('Go to cinema detail:', id);
};

onMounted(() => {
  loadCinemas();
});
</script>

<style lang="scss" scoped>
.cinemas-page {
  background: #f7f8fb;
  min-height: 100vh;
  padding: 40px 0 60px;

  .content-wrapper {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 40px;
  }

  .list-container {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.03);
    padding: 20px 40px;
  }

  .cinema-list {
    display: flex;
    flex-direction: column;
  }

  .back-top-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 48px;
    width: 48px;
    background-color: #fff;
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
    color: #15b8a6;
    border-radius: 50%;
    border: 1px solid #15b8a6;
    cursor: pointer;
    transition: all 0.3s;
    &:hover {
      background-color: #15b8a6;
      color: #fff;
      transform: translateY(-2px);
    }
  }
}
</style>