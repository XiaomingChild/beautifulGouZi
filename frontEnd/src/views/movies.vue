<template>
  <div class="movies-page">
    <div class="filter-section">
      <div class="content-wrapper">
        <div class="filter-group">
          <span class="label">类型：</span>
          <div class="options">
            <span 
              v-for="cat in categories" 
              :key="cat.value"
              class="option"
              :class="{ active: activeCategory === cat.value }"
              @click="activeCategory = cat.value"
            >{{ cat.label }}</span>
          </div>
        </div>
        <div class="filter-group">
          <span class="label">区域：</span>
          <div class="options">
            <span class="option active">全部</span>
            <span class="option">朝阳区</span>
            <span class="option">海淀区</span>
            <span class="option">丰台区</span>
            <span class="option">大兴区</span>
          </div>
        </div>
      </div>
    </div>

    <main class="content-wrapper">
      <div class="status-tabs">
        <button 
          class="tab" 
          :class="{ active: activeStatus === 'now' }"
          @click="activeStatus = 'now'"
        >正在热映</button>
        <button 
          class="tab" 
          :class="{ active: activeStatus === 'soon' }"
          @click="activeStatus = 'soon'"
        >即将上映</button>
      </div>

      <div class="movie-grid">
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
      
      <div v-if="movieList.length === 0" class="empty">
        暂无匹配电影
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { getHotMoviesApi, getUpcomingMoviesApi, getMoviesByCategoryApi } from '../api/movie';

const router = useRouter();
const activeCategory = ref('all');
const activeStatus = ref('now');
const movieList = ref<any[]>([]);

const categories = [
  { label: '全部', value: 'all' },
  { label: '剧情', value: '剧情' },
  { label: '动作', value: '动作' },
  { label: '喜剧', value: '喜剧' },
  { label: '科幻', value: '科幻' },
  { label: '动画', value: '动画' },
];

const loadMovies = async () => {
  try {
    let res: any;
    if (activeStatus.value === 'now') {
      res = await getHotMoviesApi(activeCategory.value);
    } else {
      res = await getUpcomingMoviesApi(activeCategory.value);
    }
    movieList.value = res?.content || [];
  } catch (error) {
    console.error('Failed to load movies:', error);
  }
};

watch([activeCategory, activeStatus], () => {
  loadMovies();
});

const goDetail = (id: number) => {
  router.push(`/movieDetail/${id}`);
};

onMounted(() => {
  loadMovies();
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

  .status-tabs {
    display: flex; gap: 40px; margin-bottom: 30px; border-bottom: 2px solid #e5e7eb;
    .tab {
      background: none; border: none; padding: 12px 0; font-size: 18px; font-weight: 700;
      color: #9ca3af; cursor: pointer; position: relative;
      &.active {
        color: #1f2937;
        &::after {
          content: ''; position: absolute; bottom: -2px; left: 0; width: 100%; height: 2px; background: #15b8a6;
        }
      }
    }
  }

  .movie-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 30px;

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

  .empty { text-align: center; padding: 100px 0; color: #9ca3af; }
}
</style>
