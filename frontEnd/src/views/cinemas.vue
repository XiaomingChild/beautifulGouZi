<template>
  <div class="cinemas-page">
    <div class="filter-header">
      <div class="content-wrapper">
        <div class="filter-row">
          <span class="label">行政区：</span>
          <div class="options">
            <span class="option active">全部</span>
            <span class="option" v-for="area in areas" :key="area">{{ area }}</span>
          </div>
        </div>
        <div class="filter-row">
          <span class="label">特殊厅：</span>
          <div class="options">
            <span class="option" v-for="hall in specialHalls" :key="hall">{{ hall }}</span>
          </div>
        </div>
      </div>
    </div>

    <main class="content-wrapper">
      <div class="cinema-list">
        <div v-for="cinema in cinemas" :key="cinema.id" class="cinema-card">
          <div class="cinema-main">
            <div class="info">
              <h3 class="name">{{ cinema.name }}</h3>
              <p class="address">{{ cinema.address }}</p>
              <div class="tags">
                <span v-for="tag in cinema.tags" :key="tag" class="tag">{{ tag }}</span>
              </div>
            </div>
            <div class="buy-section">
              <div class="price">
                <span class="symbol">￥</span>
                <span class="num">38</span>
                <span class="unit">起</span>
              </div>
              <button class="btn-ticket" @click="goCinemaDetail(cinema.id)">购票</button>
            </div>
          </div>
          <div class="distance">{{ cinema.distance || '1.5km' }}</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { cinemas } from '../mock/data';

const areas = ['朝阳区', '海淀区', '丰台区', '西城区', '东城区', '昌平区'];
const specialHalls = ['IMAX', '杜比全景声', '60帧', '巨幕', '4DX'];

const goCinemaDetail = (id: number) => {
  // 跳转到影院详情逻辑，目前可重定向至首页电影筛选
};
</script>

<style lang="scss" scoped>
.cinemas-page {
  background: #f7f8fb;
  min-height: 100vh;
  padding-bottom: 60px;

  .content-wrapper {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .filter-header {
    background: #fff;
    padding: 24px 0;
    border-bottom: 1px solid #f1f3f6;
    margin-bottom: 24px;

    .filter-row {
      display: flex;
      margin-bottom: 12px;
      &:last-child { margin-bottom: 0; }

      .label { width: 80px; font-size: 14px; color: #9ca3af; padding-top: 4px; }
      .options {
        flex: 1; display: flex; flex-wrap: wrap; gap: 8px;
        .option {
          padding: 4px 12px; border-radius: 4px; font-size: 14px; cursor: pointer;
          color: #4b5563;
          &:hover { color: #15b8a6; }
          &.active { background: #e9f7f9; color: #15b8a6; font-weight: 600; }
        }
      }
    }
  }

  .cinema-list {
    display: flex; flex-direction: column; gap: 20px;
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
  }
}
</style>
