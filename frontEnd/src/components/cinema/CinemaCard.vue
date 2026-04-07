<template>
  <div class="cinema-card">
    <div class="cinema-main">
      <div class="info">
        <h3 class="name" @click="$emit('go-detail', cinema.id)">{{ cinema.name }}</h3>
        <p class="address">{{ cinema.address }}</p>
        <div class="tags" v-if="cinema.tags">
          <span 
            v-for="tag in filteredTags" 
            :key="tag" 
            class="tag"
            :class="{ 
              'tech-tag': ['IMAX', '杜比全景声', '巨幕', '4DX'].includes(tag),
              'refund-tag': tag.includes('退')
            }"
          >{{ tag }}</span>
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
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { Cinema } from '../../types';

const props = defineProps<{
  cinema: Cinema;
}>();

const filteredTags = computed(() => {
  if (!props.cinema.tags) return [];
  // 过滤掉包含“改签”的标签
  return props.cinema.tags.filter(tag => !tag.includes('改签'));
});

defineEmits(['go-detail']);
</script>

<style lang="scss" scoped>
.cinema-card {
  background: #fff;
  padding: 36px 30px; // 增加左右内边距以适应背景变化
  margin: 0 -30px;    // 抵消内边距，保持视觉对齐
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border-bottom: 1px dashed #eef1f6;
  position: relative;
  z-index: 1;

  &:last-child { border-bottom: none; }

  &:hover {
    background-color: #fcfdfe; // 极淡的背景色切换
    transform: translateX(10px); // 向右轻微平移，增加动态感
    z-index: 2;
    border-bottom-color: transparent; // 隐藏虚线，避免视觉干扰
    
    .info .name { color: #15b8a6; }
    
    // 给整体加一个非常微弱的侧边指示条
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 20%;
      height: 60%;
      width: 4px;
      background: #15b8a6;
      border-radius: 0 4px 4px 0;
      opacity: 1;
    }
  }

  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 20%;
    height: 60%;
    width: 4px;
    background: #15b8a6;
    border-radius: 0 4px 4px 0;
    opacity: 0;
    transition: opacity 0.3s;
  }

  .cinema-main {
    display: flex; justify-content: space-between; align-items: flex-start;
    
    .info {
      .name { 
        font-size: 20px; 
        font-weight: 700; 
        margin-bottom: 14px; 
        color: #111827;
        cursor: pointer;
        transition: color 0.2s;
        &:hover { color: #15b8a6; }
      }
      .address { font-size: 14px; color: #6b7280; margin-bottom: 20px; }
      
      .tags {
        display: flex; gap: 10px; flex-wrap: wrap;
        .tag { 
          padding: 2px 10px;
          border-radius: 4px;
          font-size: 12px;
          font-weight: 600;
          color: #9ca3af;
          border: 1px solid #e5e7eb;
          text-transform: uppercase;
          letter-spacing: 0.5px;
          
          &.tech-tag {
            border-color: #15b8a6;
            color: #15b8a6;
            background: #e9f7f9;
          }
          
          &.refund-tag {
            border-color: #f97316;
            color: #f97316;
            background: #fff7ed;
          }
        }
      }
    }

    .buy-section {
      text-align: right;
      padding-top: 4px;
      .price {
        color: #f97316; margin-bottom: 16px;
        .symbol { font-size: 14px; }
        .num { font-size: 28px; font-weight: 800; }
        .unit { font-size: 12px; color: #9ca3af; margin-left: 2px; }
      }
      .btn-ticket {
        background: #15b8a6; color: #fff; border: none; padding: 10px 32px; border-radius: 24px;
        font-weight: 700; cursor: pointer; transition: all 0.3s;
        box-shadow: 0 4px 12px rgba(21, 184, 166, 0.2);
        &:hover { 
          transform: translateY(-2px);
          box-shadow: 0 6px 16px rgba(21, 184, 166, 0.3);
        }
      }
    }
  }
}
</style>