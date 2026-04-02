<template>
  <section class="hero" v-if="slides.length">
    <el-carousel
      height="540px"
      arrow="hover"
      indicator-position="outside"
      :interval="3000"
      :autoplay="true"
    >
      <el-carousel-item
        v-for="item in slides"
        :key="item.id"
        class="hero-item"
      >
        <div class="hero-media" :style="{ backgroundImage: `url(${item.posterUrl})` }">
          <div class="hero-mask"></div>
          <div class="hero-content">
            <p class="hero-tag">特别放映</p>
            <h1 class="hero-title">《{{ item.title }}》</h1>
            <p class="hero-subtitle">{{ item.synopsis }}</p>
            <div class="hero-actions">
              <button class="cta" type="button" @click="$emit('go-detail', item.id)">立即购票</button>
            </div>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </section>
</template>

<script setup lang="ts">
import type { Movie } from '../../types';

defineProps<{
  slides: Movie[];
}>();

defineEmits(['go-detail']);
</script>

<style lang="scss" scoped>
.hero {
  margin: 0 auto 36px;
  width: 100%;
}

.hero-media {
  position: relative;
  height: 540px;
  display: flex;
  align-items: center;
  background-size: cover;
  background-position: center;
}

.hero-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(120deg, rgba(6, 46, 60, 0.7) 0%, rgba(6, 46, 60, 0.4) 100%);
}

.hero-content {
  position: relative;
  z-index: 1;
  color: #ffffff;
  max-width: 600px;
  padding: 0 72px;
}

.hero-title {
  font-size: 42px;
  font-weight: 800;
  margin-bottom: 16px;
}

.hero-subtitle {
  font-size: 16px;
  line-height: 1.6;
  opacity: 0.9;
  margin-bottom: 24px;
}

.cta {
  padding: 12px 32px;
  border-radius: 8px;
  border: none;
  background: #15b8a6;
  color: #fff;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
  &:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(21, 184, 166, 0.3); }
}
</style>
