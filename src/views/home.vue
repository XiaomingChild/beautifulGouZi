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
import { reactive } from 'vue'
</script>


<style lang="scss" scoped>

</style>
