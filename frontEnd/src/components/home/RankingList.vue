<template>
  <div class="rank-block">
    <div class="rank-head">
      <h3>高分排行</h3>
      <span class="rank-tag">Top 20</span>
    </div>
    <ol class="rank-list">
      <li
        v-for="(item, index) in ranking.slice(0, 20)"
        :key="item.id"
        class="rank-item"
        :class="{ highlight: index === 0 }"
        @click="$emit('go-detail', item.id)"
      >
        <span class="rank-no" :class="{ top: index < 3 }">{{ index + 1 }}</span>
        <div class="rank-info">
          <p class="rank-title">{{ item.title }}</p>
          <p class="rank-amount">{{ item.rating }} 分</p>
        </div>
        <img
          v-if="index === 0 && item.posterUrl"
          :src="item.posterUrl"
          alt="海报"
          class="rank-thumb"
        />
      </li>
    </ol>
  </div>
</template>

<script setup lang="ts">
import type { Movie } from '../../types';

defineProps<{
  ranking: Movie[];
}>();

defineEmits(['go-detail']);
</script>

<style lang="scss" scoped>
.rank-block {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.rank-head {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;
  h3 { font-size: 18px; font-weight: 700; margin: 0; }
  .rank-tag { font-size: 12px; color: #15b8a6; background: #e9f7f6; padding: 2px 8px; border-radius: 10px; }
}

.rank-list {
  list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 12px;
}

.rank-item {
  display: flex; align-items: center; gap: 12px; padding: 8px; border-radius: 8px; transition: all 0.2s;
  cursor: pointer;
  &:hover { background: #f9fafb; }
  &.highlight { background: #f0fdfa; }
}

.rank-no {
  width: 24px; height: 24px; display: flex; align-items: center; justify-content: center;
  font-weight: 700; font-size: 14px; color: #9ca3af;
  &.top { color: #f97316; }
}

.rank-info { flex: 1; .rank-title { font-size: 14px; font-weight: 600; margin: 0; } .rank-amount { font-size: 12px; color: #6b7280; margin: 0; } }
.rank-thumb { width: 60px; height: 80px; border-radius: 4px; object-fit: cover; }
</style>
