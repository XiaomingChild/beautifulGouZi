<template>
  <article
    class="card"
    @click="$emit('go-detail', movie.id)"
  >
    <div class="poster" :style="{ backgroundImage: `url(${movie.posterUrl})` }">
      <span class="rating" v-if="!isUpcoming">{{ movie.rating }}</span>
      <span class="rating" v-else>想看</span>
      <div 
        v-if="!isUpcoming"
        class="favorite-star" 
        :class="{ active: isFavorited }" 
        @click.stop="$emit('toggle-favorite', movie.id)"
      >
        <el-icon><StarFilled /></el-icon>
      </div>
    </div>
    <div class="card-body">
      <h3 class="card-title">{{ movie.title }}</h3>
      <p v-if="!isUpcoming" class="card-subtitle">{{ movie.durationMinutes }}分钟</p>
      <div class="tags">
        <span v-for="tag in formatTags(movie.genre)" :key="tag" class="tag">{{ tag }}</span>
      </div>
      <div v-if="isUpcoming" class="card-meta">
        <span class="release">上映 {{ formatDate(movie.releaseDate) }}</span>
      </div>
      <button
        v-if="!isUpcoming"
        class="buy"
        type="button"
        @click.stop="$emit('go-detail', movie.id)"
      >
        购票
      </button>
      <button
        v-else
        class="wish"
        :class="{ active: isFavorited }"
        type="button"
        @click.stop="$emit('toggle-favorite', movie.id)"
      >
        {{ isFavorited ? '已想看' : '想看' }}
      </button>
    </div>
  </article>
</template>

<script setup lang="ts">
import { StarFilled } from '@element-plus/icons-vue';
import type { Movie } from '../../types';

defineProps<{
  movie: Movie;
  isFavorited: boolean;
  isUpcoming?: boolean;
}>();

defineEmits(['go-detail', 'toggle-favorite']);

const formatTags = (genre: string) => {
  if (!genre) return [];
  return genre.split(',');
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN');
};
</script>

<style lang="scss" scoped>
.card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  cursor: pointer;
  transition: all 0.3s;
  &:hover { transform: translateY(-8px); box-shadow: 0 12px 24px rgba(0,0,0,0.1); }
}

.poster {
  height: 280px;
  background-size: cover;
  background-position: center;
  position: relative;
  .rating {
    position: absolute; top: 10px; left: 10px;
    background: rgba(0,0,0,0.7); color: #ffde6a;
    padding: 4px 8px; border-radius: 4px; font-weight: 700; font-size: 12px;
  }
}

.favorite-star {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 32px;
  height: 32px;
  background: rgba(0,0,0,0.4);
  backdrop-filter: blur(4px);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 2;
  border: 1px solid rgba(255, 255, 255, 0.2);

  .el-icon {
    font-size: 18px;
  }

  &:hover {
    background: rgba(0,0,0,0.6);
    transform: scale(1.1);
  }

  &.active {
    color: #ffc600;
    background: rgba(255, 198, 0, 0.15);
    border-color: rgba(255, 198, 0, 0.4);
    box-shadow: 0 0 12px rgba(255, 198, 0, 0.2);
    filter: drop-shadow(0 0 2px rgba(255, 198, 0, 0.5));
  }
}

.card-body {
  padding: 12px;
  .card-title { font-size: 16px; font-weight: 700; color: #111827; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  .card-subtitle { font-size: 12px; color: #6b7280; margin-bottom: 8px; }
}

.tags {
  display: flex; gap: 4px; flex-wrap: wrap; margin-bottom: 12px;
  .tag { background: #f3f4f6; color: #4b5563; font-size: 10px; padding: 2px 4px; border-radius: 4px; }
}

.buy, .wish {
  width: 100%; padding: 8px; border-radius: 6px; font-weight: 700; cursor: pointer; font-size: 13px;
}

.buy { background: #15b8a6; color: #fff; border: none; }
.wish { background: #fff; border: 1px solid #f97316; color: #f97316; }
.wish.active { background: #f97316; color: #fff; border-color: #f97316; }

.card-meta {
    margin-bottom: 12px;
    .release {
        font-size: 12px;
        color: #6b7280;
    }
}
</style>
