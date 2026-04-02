<template>
  <main class="home-page" v-loading="loading">
    <HeroCarousel 
      v-if="heroSlides.length > 0" 
      :slides="heroSlides" 
      @go-detail="goDetail" 
    />

    <section class="section content-grid">
      <div class="main-column">
        <div class="section-head">
          <h2>电影分类</h2>
        </div>
        
        <CategoryTabs 
          :model-value="activeCategory" 
          :categories="categories" 
          @update:model-value="handleCategoryChange"
        />

        <!-- 正在热映区块 -->
        <div class="sub-section">
          <div class="section-head">
            <h2>正在热映</h2>
            <span class="section-sub">影院热映精选</span>
          </div>
          <div class="cards" v-if="hotMovies.length > 0">
            <MovieCard 
              v-for="movie in hotMovies" 
              :key="'hot-' + movie.id" 
              :movie="movie" 
              :is-favorited="isFavorited(movie.id)"
              @go-detail="goDetail"
              @toggle-favorite="handleToggleFavorite"
            />
          </div>
          <el-empty v-else-if="!loading" :image-size="100" description="暂无热映电影" />
        </div>

        <!-- 即将上映区块 -->
        <div class="sub-section">
          <div class="section-head">
            <h2>即将上映</h2>
            <span class="section-sub">提前锁定想看</span>
          </div>
          <div class="cards upcoming-cards" v-if="upcomingMovies.length > 0">
            <MovieCard 
              v-for="movie in upcomingMovies" 
              :key="'soon-' + movie.id" 
              :movie="movie" 
              :is-favorited="isFavorited(movie.id)"
              :is-upcoming="true"
              @go-detail="goDetail"
              @toggle-favorite="handleToggleFavorite"
            />
          </div>
          <el-empty v-else-if="!loading" :image-size="100" description="暂无上映计划" />
        </div>
      </div>

      <!-- 右侧侧边栏 -->
      <aside class="sidebar">
        <RankingList 
          v-if="dynamicRanking.length > 0" 
          :ranking="dynamicRanking" 
          @go-detail="goDetail" 
        />
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import HeroCarousel from '../components/home/HeroCarousel.vue';
import MovieCard from '../components/home/MovieCard.vue';
import RankingList from '../components/home/RankingList.vue';
import CategoryTabs from '../components/home/CategoryTabs.vue';
import { 
  getHotMoviesApi, 
  getUpcomingMoviesApi, 
  getMoviesByCategoryApi,
  getMovieRankingApi,
  toggleFavoriteApi,
  getUserFavoritesApi
} from '../api/movie';
import { useUserStore } from '../store/userInfo';
import type { Movie } from '../types';

const router = useRouter();
const userStore = useUserStore();

const hotMovies = ref<Movie[]>([]);
const upcomingMovies = ref<Movie[]>([]);
const heroSlides = ref<Movie[]>([]);
const dynamicRanking = ref<Movie[]>([]);
const favoriteIds = ref<number[]>([]);
const loading = ref(false);

const categories = [
  { label: '全部', value: 'all' },
  { label: '正在热映', value: 'now_playing' },
  { label: '即将上映', value: 'coming_soon' },
  { label: '剧情', value: '剧情' },
  { label: '喜剧', value: '喜剧' },
  { label: '动作', value: '动作' },
  { label: '爱情', value: '爱情' },
  { label: '科幻', value: '科幻' },
  { label: '动画', value: '动画' },
];

const activeCategory = ref(categories[0].value);

const loadInitialData = async () => {
  loading.value = true;
  try {
    // 并行获取初始数据
    const [hotRes, soonRes, rankRes] = await Promise.all([
      getHotMoviesApi(),
      getUpcomingMoviesApi(),
      getMovieRankingApi()
    ]);

    hotMovies.value = hotRes?.content || [];
    upcomingMovies.value = soonRes?.content || [];
    dynamicRanking.value = rankRes || [];
    
    // 设置轮播图
    if (hotMovies.value.length > 0) {
      heroSlides.value = hotMovies.value.slice(0, 3);
    }

    // 获取收藏列表
    if (userStore.state.id) {
      const favRes = await getUserFavoritesApi(Number(userStore.state.id));
      favoriteIds.value = favRes || [];
      userStore.setUserInfo({ selected: favoriteIds.value });
    }
  } catch (error) {
    console.error('Failed to load initial data:', error);
  } finally {
    loading.value = false;
  }
};

const handleCategoryChange = async (value: string) => {
  activeCategory.value = value;
  loading.value = true;
  try {
    if (value === 'all') {
      await loadInitialData();
    } else if (value === 'now_playing') {
      const res = await getHotMoviesApi();
      hotMovies.value = res?.content || [];
      upcomingMovies.value = [];
    } else if (value === 'coming_soon') {
      const res = await getUpcomingMoviesApi();
      upcomingMovies.value = res?.content || [];
      hotMovies.value = [];
    } else {
      const res = await getMoviesByCategoryApi(value);
      hotMovies.value = res?.content || [];
      upcomingMovies.value = [];
    }
  } catch (error) {
    console.error('Filter category failed:', error);
  } finally {
    loading.value = false;
  }
};

const handleToggleFavorite = async (movieId: number) => {
  if (!userStore.state.id) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  try {
    const res = await toggleFavoriteApi(Number(userStore.state.id), movieId);
    favoriteIds.value = res || [];
    userStore.setUserInfo({ selected: favoriteIds.value });
    
    ElMessage.success(favoriteIds.value.includes(movieId) ? '已加入收藏' : '已取消收藏');
  } catch (error) {
    console.error('Toggle favorite failed:', error);
  }
};

const isFavorited = (movieId: number) => {
  return favoriteIds.value.includes(movieId);
};

const goDetail = (id: number) => {
  router.push(`/movieDetail/${id}`);
};

onMounted(() => {
  loadInitialData();
});
</script>

<style lang="scss" scoped>
.home-page {
  background: #f7f8fb;
  min-height: 100vh;
  padding: 0 0 108px;
}

.section {
  max-width: 1400px;
  margin: 0 auto 32px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 32px;
  padding: 0 20px;
}

.section-head {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 24px;
  h2 { font-size: 24px; font-weight: 700; color: #111827; margin: 0; }
  .section-sub { font-size: 14px; color: #6b7280; }
}

.sub-section { margin-bottom: 48px; }

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: sticky;
  top: 88px;
  align-self: start;
}
</style>
