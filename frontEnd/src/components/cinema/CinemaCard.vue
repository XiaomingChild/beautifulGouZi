<template>
  <div class="cinema-card">
    <div class="cinema-main">
      <div class="info">
        <h3 class="name">{{ cinema.name }}</h3>
        <p class="address">{{ cinema.address }}</p>
        <div class="tags" v-if="cinema.tags">
          <span v-for="tag in cinema.tags" :key="tag" class="tag">{{ tag }}</span>
        </div>
      </div>
      <div class="buy-section">
        <div class="price">
          <span class="symbol">￥</span>
          <span class="num">38</span>
          <span class="unit">起</span>
        </div>
        <button class="btn-ticket" @click="$emit('go-detail', cinema.id)">购票</button>
      </div>
    </div>
    <div class="distance">{{ cinema.distance || '1.5km' }}</div>
  </div>
</template>

<script setup lang="ts">
import type { Cinema } from '../../types';

defineProps<{
  cinema: Cinema;
}>();

defineEmits(['go-detail']);
</script>

<style lang="scss" scoped>
.cinema-card {
  background: #fff; border-radius: 12px; padding: 24px;
  position: relative; transition: all 0.3s;
  &:hover { box-shadow: 0 8px 24px rgba(0,0,0,0.05); }

  .cinema-main {
    display: flex; justify-content: space-between; align-items: center;
    
    .info {
      .name { font-size: 18px; font-weight: 700; margin-bottom: 10px; color: #1f2937; }
      .address { font-size: 14px; color: #6b7280; margin-bottom: 12px; }
      .tags {
        display: flex; gap: 8px;
        .tag { border: 1px solid #e5e7eb; color: #9ca3af; font-size: 11px; padding: 2px 8px; border-radius: 4px; }
      }
    }

    .buy-section {
      text-align: right;
      .price {
        color: #f97316; margin-bottom: 12px;
        .symbol { font-size: 12px; }
        .num { font-size: 24px; font-weight: 800; }
        .unit { font-size: 12px; color: #9ca3af; }
      }
      .btn-ticket {
        background: #15b8a6; color: #fff; border: none; padding: 8px 24px; border-radius: 20px;
        font-weight: 600; cursor: pointer; transition: all 0.2s;
        &:hover { opacity: 0.9; }
      }
    }
  }

  .distance {
    position: absolute; right: 24px; top: 24px; font-size: 12px; color: #9ca3af;
  }
}
</style>
