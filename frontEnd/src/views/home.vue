<template>
    <div class="container">
        <div class="container-nav">
            <HomeNav />
        </div>
         <div class="container-content">
            <HomeMovie />
         </div>
    </div>
  
  
</template>

<script setup lang="ts">
import HomeNav from '../components/homeNav.vue'
import HomeMovie from './homeMovie.vue';
import { onMounted,reactive } from 'vue';
import { AMap, getAddressByLngLat } from '../utils/amp';
const state = reactive({
  laln: '',//经纬度
  position:'',//省市县
});
onMounted( async () => {
  AMap();
  //把当前的城市存到缓存中
});
</script>


<style lang="scss" scoped>
.container {
  background: #f7f8fb;
  min-height: 100vh;
}

.container-nav {
  position: sticky;
  top: 0;
  z-index: 20;
}
</style>
