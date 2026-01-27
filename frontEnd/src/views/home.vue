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
