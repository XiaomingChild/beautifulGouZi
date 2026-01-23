<template>
  <div class="container">
    <div v-for="item in state.dataList" :key="item.id">
        <div @click="handleMovieDetail">{{item.title}}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

  
import { onMounted } from 'vue'
import { reactive } from 'vue'
interface MovieItem {
  id:number;
  title:string;
}
const router = useRouter()
const state = reactive({
  dataList:[] as MovieItem[]
})
onMounted(()=>{
  state.dataList = [
    {id:1,title:'电影1'},
    {id:2,title:'电影2'},
    {id:3,title:'电影3'},
    {id:4,title:'电影4'},
    {id:5,title:'电影5'},
  ]
})
//跳转电影详情页
const handleMovieDetail = ()=>{
    console.log('跳转电影详情页');
    router.push('/homeMovie')
}
</script>


<style lang="scss" scoped>

</style>
