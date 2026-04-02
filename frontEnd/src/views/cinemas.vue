<template>
  <div class="cinemas-page">
    <CinemaFilter 
      :areas="areas" 
      :special-halls="specialHalls" 
      v-model:active-area="activeArea"
      v-model:active-hall="activeHall"
    />

    <main class="content-wrapper">
      <div class="cinema-list">
        <CinemaCard 
          v-for="cinema in cinemas" 
          :key="cinema.id" 
          :cinema="cinema" 
          @go-detail="goCinemaDetail"
        />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import CinemaFilter from '../components/cinema/CinemaFilter.vue';
import CinemaCard from '../components/cinema/CinemaCard.vue';
import { cinemas } from '../mock/data';

const areas = ['朝阳区', '海淀区', '丰台区', '西城区', '东城区', '昌平区'];
const specialHalls = ['IMAX', '杜比全景声', '60帧', '巨幕', '4DX'];

const activeArea = ref('全部');
const activeHall = ref('IMAX');

const goCinemaDetail = (id: number) => {
  // 跳转到影院详情逻辑
  console.log('Go to cinema detail:', id);
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

  .cinema-list {
    display: flex; flex-direction: column; gap: 20px;
  }
}
</style>
