<template>
  <main class="detail-page" v-if="movie">
    <!-- 电影头部背景与核心信息 -->
    <section class="banner-section" :style="{ backgroundImage: `url(${movie.posterUrl})` }">
      <div class="banner-mask"></div>
      <div class="content-wrapper hero-content">
        <div class="poster-box">
          <img :src="movie.posterUrl" :alt="movie.title" class="main-poster" />
        </div>
        <div class="info-box">
          <h1 class="title">{{ movie.title }}</h1>
          <div class="meta-tags">
            <span v-for="tag in formatTags(movie.genre)" :key="tag" class="type-tag">{{ tag }}</span>
          </div>
          <div class="meta-line">{{ movie.durationMinutes }}分钟 / {{ formatDate(movie.releaseDate) }} 上映</div>
          
          <div class="action-row">
            <div class="score-card">
              <span class="score">{{ movie.rating || '暂无' }}</span>
              <span class="label">评分</span>
            </div>
            <div class="btn-group">
              <button class="btn-action wish" :class="{ active: isWished }" @click="toggleWish">
                <el-icon><Star /></el-icon> {{ isWished ? '已想看' : '想看' }}
              </button>
              <button class="btn-action rate">
                <el-icon><ChatDotRound /></el-icon> 评分
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <div class="content-wrapper main-layout">
      <!-- 左侧：剧情与演职人员 -->
      <div class="left-col">
        <section class="info-section">
          <h2 class="section-title">剧情简介</h2>
          <p class="description">{{ movie.synopsis }}</p>
        </section>

        <!-- 排片列表 -->
        <section class="schedule-section">
          <h2 class="section-title">影院排片</h2>
          <div v-if="cinemaSchedules.length === 0" class="empty-state">
            <el-empty description="暂无排片信息" />
          </div>
          <div v-else class="cinema-list">
            <div v-for="item in cinemaSchedules" :key="item.cinema.id" class="cinema-card">
              <div class="cinema-info">
                <h3 class="cinema-name">{{ item.cinema.name }}</h3>
                <p class="cinema-address">{{ item.cinema.address }}</p>
                <div class="cinema-tags">
                  <span v-for="tag in formatTags(item.cinema.tags)" :key="tag" class="c-tag">{{ tag }}</span>
                </div>
              </div>
              <div class="schedules-grid">
                <div 
                  v-for="sc in item.schedules" 
                  :key="sc.id" 
                  class="sc-item"
                  @click="goSeatSelection(sc)"
                >
                  <div class="time">{{ formatTime(sc.startTime) }}</div>
                  <div class="type">{{ sc.type }}</div>
                  <div class="hall">{{ sc.hallName }}</div>
                  <div class="price">￥{{ sc.price }}</div>
                  <button class="buy-btn">选座购票</button>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- 右侧：侧边栏（预留，可放相关推荐等） -->
      <aside class="right-col">
        <div class="rank-block">
          <h3 class="side-title">相关推荐</h3>
          <!-- 预留 -->
        </div>
      </aside>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Star, ChatDotRound } from '@element-plus/icons-vue';
import { getMovieDetailApi, getMovieSchedulesApi } from '../api/movie';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();
const movieId = route.params.id as string;

const movie = ref<any>(null);
const cinemaSchedules = ref<any[]>([]);
const isWished = ref(false);

const loadData = async () => {
  try {
    // 1. 获取电影详情
    const movieRes: any = await getMovieDetailApi(movieId);
    if (movieRes) {
      movie.value = movieRes;
    }

    // 2. 获取排片信息
    const scheduleRes: any = await getMovieSchedulesApi(movieId);
    cinemaSchedules.value = scheduleRes || [];
  } catch (error) {
    console.error('Failed to load movie details:', error);
    ElMessage.error('加载失败');
  }
};

const formatTags = (tags: string) => {
  return tags ? tags.split(',') : [];
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN');
};

const formatTime = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', hour12: false });
};

const toggleWish = () => {
  isWished.value = !isWished.value;
  ElMessage.success(isWished.value ? '已加入想看列表' : '已取消想看');
};

const goSeatSelection = (schedule: any) => {
  // 跳转到选座页，携带场次 ID
  router.push({
    path: '/seatSelection',
    query: { scheduleId: schedule.id }
  });
};

onMounted(() => {
  loadData();
});
</script>

<style lang="scss" scoped>
.detail-page {
  background: #fdfdfd;
  min-height: 100vh;
}

.banner-section {
  position: relative;
  height: 420px;
  background-size: cover;
  background-position: center;
  color: #fff;
  display: flex;
  align-items: center;

  .banner-mask {
    position: absolute;
    inset: 0;
    background: linear-gradient(to right, rgba(0,0,0,0.8) 20%, rgba(0,0,0,0.4) 100%);
  }

  .hero-content {
    position: relative;
    z-index: 1;
    display: flex;
    gap: 40px;
    align-items: flex-end;
    padding-bottom: 40px;
  }

  .poster-box {
    width: 240px;
    height: 330px;
    border: 4px solid #fff;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 20px 40px rgba(0,0,0,0.4);
    .main-poster { width: 100%; height: 100%; object-fit: cover; }
  }

  .info-box {
    flex: 1;
    .title { font-size: 36px; font-weight: 800; margin-bottom: 8px; }
    .en-name { font-size: 18px; opacity: 0.8; margin-bottom: 20px; }
    .meta-tags { display: flex; gap: 8px; margin-bottom: 12px;
      .type-tag { background: rgba(255,255,255,0.2); padding: 4px 12px; border-radius: 4px; font-size: 13px; }
    }
    .meta-line { font-size: 14px; opacity: 0.9; margin-bottom: 30px; }
  }

  .action-row {
    display: flex;
    align-items: center;
    gap: 30px;

    .score-card {
      display: flex; flex-direction: column;
      .score { font-size: 32px; color: #ffc600; font-weight: 800; line-height: 1; }
      .label { font-size: 12px; opacity: 0.7; margin-top: 4px; }
    }

    .btn-group {
      display: flex; gap: 12px;
      .btn-action {
        background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.3);
        color: #fff; padding: 10px 24px; border-radius: 6px; cursor: pointer;
        display: flex; align-items: center; gap: 8px; font-weight: 600;
        transition: all 0.3s;
        &:hover { background: rgba(255,255,255,0.25); }
        &.active { background: #ff4d4f; border-color: #ff4d4f; }
      }
    }
  }
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.main-layout {
  display: grid;
  grid-template-columns: 8fr 4fr;
  gap: 60px;
  padding-top: 40px;
  padding-bottom: 100px;
}

.section-title {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 20px;
  padding-left: 12px;
  border-left: 4px solid #15b8a6;
}

.info-section {
  margin-bottom: 50px;
  .description { font-size: 15px; color: #4b5563; line-height: 1.8; }
}

.cinema-card {
  margin-bottom: 40px;
  background: #fff;
  border: 1px solid #f1f3f6;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.02);

  .cinema-name { font-size: 18px; font-weight: 700; margin-bottom: 8px; }
  .cinema-address { font-size: 13px; color: #9ca3af; margin-bottom: 12px; }
  .cinema-tags { display: flex; gap: 6px; margin-bottom: 20px;
    .c-tag { font-size: 11px; color: #15b8a6; border: 1px solid #15b8a6; padding: 1px 6px; border-radius: 4px; }
  }
}

.schedules-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 16px;

  .sc-item {
    border: 1px solid #f1f3f6; border-radius: 8px; padding: 16px; text-align: center;
    cursor: pointer; transition: all 0.2s;
    &:hover { border-color: #15b8a6; background: #f0fdfa; transform: translateY(-2px); }

    .time { font-size: 20px; font-weight: 700; color: #111827; margin-bottom: 4px; }
    .type { font-size: 12px; color: #6b7280; margin-bottom: 4px; }
    .hall { font-size: 12px; color: #9ca3af; margin-bottom: 8px; }
    .price { font-size: 16px; color: #ff4d4f; font-weight: 700; margin-bottom: 12px; }
    .buy-btn {
      width: 100%; background: #15b8a6; color: #fff; border: none;
      padding: 6px 0; border-radius: 6px; font-size: 13px; font-weight: 600;
    }
  }
}

.right-col {
  .side-title { font-size: 18px; font-weight: 700; margin-bottom: 20px; }
}
</style>