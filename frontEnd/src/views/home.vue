<template>
  <div class="home-view">
    <HomeMovie />
  </div>
</template>

<script setup lang="ts">
import HomeMovie from './homeMovie.vue';
import { onMounted, reactive } from 'vue';
import { AMap } from '../utils/amp';

const state = reactive({
  laln: '', // 经纬度
  position: '', // 省市县
});

onMounted(async () => {
  AMap();
  // 把当前的城市存到缓存中
});
</script>

<style lang="scss" scoped>
.home-view {
  min-height: calc(100vh - 68px);
}
</style>
