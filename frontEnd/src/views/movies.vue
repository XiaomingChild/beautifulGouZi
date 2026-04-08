<template>
  <div class="movies-page">
    <!-- 返回顶部悬浮按钮 -->
    <el-backtop :right="60" :bottom="100">
      <div class="back-top-btn">
        <svg viewBox="0 0 1024 1024" width="24" height="24">
          <path fill="currentColor" d="M512 320L192 640h640L512 320z"></path>
        </svg>
      </div>
    </el-backtop>

    <div class="filter-section">
      <div class="content-wrapper">
        <div class="status-tabs-top">
          <button 
            class="tab" 
            :class="{ active: activeStatus === 'now' }"
            @click="handleStatusChange('now')"
          >正在热映</button>
          <button 
            class="tab" 
            :class="{ active: activeStatus === 'soon' }"
            @click="handleStatusChange('soon')"
          >即将上映</button>
        </div>
        
        <div class="filter-group">
          <span class="label">类型：</span>
          <div class="options">
            <span 
              v-for="cat in categories" 
              :key="cat.value"
              class="option"
              :class="{ active: activeCategory === cat.value }"
              @click="handleCategoryChange(cat.value)"
            >{{ cat.label }}</span>
          </div>
        </div>
      </div>
    </div>

    <main class="content-wrapper">
      <div class="movie-grid" v-if="movieList.length > 0">
        <article 
          v-for="movie in movieList" 
          :key="movie.id" 
          class="movie-card"
          @click="goDetail(movie.id)"
        >
          <div class="poster">
            <img :src="movie.posterUrl" alt="" />
            <div class="score" v-if="activeStatus === 'now'">{{ movie.rating }}</div>
            <div class="wish" v-else>期待上映</div>
          </div>
          <div class="info">
            <h3 class="title">{{ movie.title }}</h3>
            <p class="type">{{ movie.genre }}</p>
            <button class="btn-action" :class="activeStatus === 'now' ? 'now' : 'soon'">
              {{ activeStatus === 'now' ? '购票' : '预售' }}
            </button>
          </div>
        </article>
      </div>
      
      <div v-if="loading && movieList.length === 0" class="loading-state">
        正在加载中...
      </div>
      <div v-else-if="movieList.length === 0" class="empty">
        暂无匹配电影
      </div>

      <div ref="loadMoreRef" class="load-more-trigger">
        <span v-if="loading && movieList.length > 0">加载中...</span>
        <span v-else-if="noMore && movieList.length > 0">没有更多了</span>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { getHotMoviesApi, getUpcomingMoviesApi } from '../api/movie';

const router = useRouter();
const route = useRoute();

const activeCategory = ref('all');
const activeStatus = ref(route.query.status === 'soon' ? 'soon' : 'now');
const movieList = ref<any[]>([]);
const loading = ref(false);
const noMore = ref(false);
const page = ref(0);
const pageSize = 12;

const categories = [
  { label: '全部', value: 'all' },
  { label: '剧情', value: '剧情' },
  { label: '动作', value: '动作' },
  { label: '喜剧', value: '喜剧' },
  { label: '科幻', value: '科幻' },
  { label: '动画', value: '动画' },
];

const loadMoreRef = ref<HTMLElement | null>(null);
let observer: IntersectionObserver | null = null;

const loadMovies = async (isReset = false) => {
  if (loading.value || (noMore.value && !isReset)) return;
  
  loading.value = true;
  if (isReset) {
    page.value = 0;
    movieList.value = [];
    noMore.value = false;
  }

  try {
    let res: any;
    if (activeStatus.value === 'now') {
      res = await getHotMoviesApi(activeCategory.value, page.value, pageSize);
    } else {
      res = await getUpcomingMoviesApi(activeCategory.value, page.value, pageSize);
    }
    
    const newMovies = res?.content || [];
    if (newMovies.length < pageSize) {
      noMore.value = true;
    }
    
    movieList.value = [...movieList.value, ...newMovies];
    page.value++;
  } catch (error) {
    console.error('Failed to load movies:', error);
  } finally {
    loading.value = false;
  }
};

const handleStatusChange = (status: string) => {
  if (activeStatus.value === status) return;
  activeStatus.value = status;
  router.replace({ query: { ...route.query, status } });
  loadMovies(true);
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleCategoryChange = (category: string) => {
  if (activeCategory.value === category) return;
  activeCategory.value = category;
  loadMovies(true);
};

const goDetail = (id: number) => {
  router.push(`/movieDetail/${id}`);
};

onMounted(() => {
  loadMovies(true);
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && !loading.value && !noMore.value) {
      loadMovies();
    }
  }, { threshold: 0.1 });

  if (loadMoreRef.value) {
    observer.observe(loadMoreRef.value);
  }
});

onUnmounted(() => {
  if (observer) observer.disconnect();
});

watch(() => route.query.status, (newStatus) => {
  const status = newStatus === 'soon' ? 'soon' : 'now';
  if (status !== activeStatus.value) {
    activeStatus.value = status;
    loadMovies(true);
  }
});
</script>

<style lang="scss" scoped>
.movies-page {
  background: #f7f8fb;
  min-height: 100vh;
  padding-bottom: 60px;

  .content-wrapper {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .filter-section {
    background: #fff;
    padding: 30px 0;
    margin-bottom: 30px;
    border-bottom: 1px solid #f1f3f6;

    .status-tabs-top {
      display: flex;
      justify-content: center;
      gap: 100px;
      margin-bottom: 30px;
      border-bottom: 2px solid #e5e7eb;
      .tab {
        background: none;
        border: none;
        padding: 12px 20px;
        font-size: 20px;
        font-weight: 700;
        color: #9ca3af;
        cursor: pointer;
        position: relative;
        transition: all 0.3s;
        &.active {
          color: #15b8a6;
          &::after {
            content: '';
            position: absolute;
            bottom: -2px;
            left: 0;
            width: 100%;
            height: 3px;
            background: #15b8a6;
          }
        }
      }
    }

    .filter-group {
      display: flex;
      margin-bottom: 16px;
      &:last-child { margin-bottom: 0; }
      .label { color: #9ca3af; font-size: 14px; width: 60px; padding-top: 4px; }
      .options {
        flex: 1; display: flex; flex-wrap: wrap; gap: 10px;
        .option {
          padding: 4px 12px; border-radius: 4px; font-size: 14px; cursor: pointer;
          color: #4b5563; transition: all 0.2s;
          &:hover { color: #15b8a6; }
          &.active { background: #15b8a6; color: #fff; }
        }
      }
    }
  }

  .movie-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 25px; // 稍微缩小间距以适应 5 列布局
    .movie-card {
      background: #fff; border-radius: 12px; overflow: hidden; cursor: pointer;
      transition: all 0.3s;
      &:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.05); }
      .poster {
        position: relative; aspect-ratio: 2/3;
        img { width: 100%; height: 100%; object-fit: cover; }
        .score, .wish {
          position: absolute; left: 10px; bottom: 10px;
          padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: 800;
          background: rgba(0,0,0,0.6); backdrop-filter: blur(4px);
        }
        .score { color: #f97316; }
        .wish { color: #fff; }
      }
      .info {
        padding: 16px;
        .title { font-size: 16px; font-weight: 700; margin-bottom: 8px; 
          white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
        }
        .type { font-size: 13px; color: #9ca3af; margin-bottom: 16px; }
        .btn-action {
          width: 100%; height: 36px; border-radius: 6px; border: none; font-weight: 600; cursor: pointer;
          &.now { background: #15b8a6; color: #fff; }
          &.soon { background: #fff; border: 1px solid #15b8a6; color: #15b8a6; }
        }
      }
    }
  }

  .loading-state, .empty, .load-more-trigger {
    text-align: center;
    padding: 30px 0;
    color: #9ca3af;
  }

  .back-top-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
    background-color: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    color: #15b8a6;
    font-weight: 700;
    border-radius: 50%;
    border: 1px solid #15b8a6;
    cursor: pointer;
  }
}
</style>