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
          v-for="movie in filteredMovies" 
          :key="movie.id" 
          class="movie-card"
          @click="goDetail(movie.id)"
        >
          <div class="poster">
            <img :src="movie.imgUrl" alt="" />
            <div class="score" v-if="movie.status === 'now'">{{ movie.rating }}</div>
            <div class="wish" v-else>{{ movie.wishes }}人想看</div>
          </div>
          <div class="info">
            <h3 class="title">{{ movie.title }}</h3>
            <p class="type">{{ movie.tags.join('/') }}</p>
            <button class="btn-action" :class="movie.status">
              {{ movie.status === 'now' ? '购票' : '预售' }}
            </button>
          </div>
        </article>
      </div>
      
      <div v-if="filteredMovies.length === 0" class="empty">
        暂无匹配电影
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { movies } from '../mock/data';

const router = useRouter();
const activeCategory = ref('all');
const activeStatus = ref('now');

const categories = [
  { label: '全部', value: 'all' },
  { label: '正在热映', value: 'now_playing' },
  { label: '剧情', value: 'drama' },
  { label: '动作', value: 'action' },
  { label: '喜剧', value: 'comedy' },
  { label: '科幻', value: 'scifi' },
  { label: '动画', value: 'animation' },
];

const filteredMovies = computed(() => {
  return movies.filter(m => {
    const statusMatch = m.status === activeStatus.value;
    const catMatch = activeCategory.value === 'all' || m.category === activeCategory.value;
    return statusMatch && catMatch;
  });
});

const goDetail = (id: number) => {
  router.push(`/movieDetail/${id}`);
};
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
