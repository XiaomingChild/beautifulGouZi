<template>
  <main class="home-page">
    <section class="hero" v-if="heroSlides.length">
      <el-carousel
        height="540px"
        arrow="hover"
        indicator-position="outside"
        :interval="3000"
        :autoplay="true"
      >
        <el-carousel-item
          v-for="item in heroSlides"
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
                <button class="cta" type="button" @click="goDetail(item.id)">立即购票</button>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <section class="section content-grid">
      <div class="main-column">
        <div class="section-head">
          <h2>电影分类</h2>
        </div>
        <div class="chips">
          <button
            v-for="item in categories"
            :key="item.value"
            class="chip"
            :class="{ active: item.value === activeCategory }"
            type="button"
            @click="handleCategoryClick(item.value, item.label)"
          >
            {{ item.label }}
          </button>
        </div>

        <div class="sub-section">
          <div class="section-head">
            <h2>正在热映</h2>
            <span class="section-sub">影院热映精选</span>
          </div>
          <div class="cards">
            <article
              v-for="movie in hotMovies"
              :key="movie.id"
              class="card"
              @click="goDetail(movie.id)"
            >
              <div class="poster" :style="{ backgroundImage: `url(${movie.posterUrl})` }">
                <span class="rating">{{ movie.rating }}</span>
                <div 
                  class="favorite-star" 
                  :class="{ active: isFavorited(movie.id) }" 
                  @click.stop="handleToggleFavorite(movie.id)"
                >
                  <el-icon><StarFilled /></el-icon>
                </div>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ movie.title }}</h3>
                <p class="card-subtitle">{{ movie.durationMinutes }}分钟</p>
                <div class="tags">
                  <span v-for="tag in formatTags(movie.genre)" :key="tag" class="tag">{{ tag }}</span>
                </div>
                <button class="buy" type="button" @click.stop="goDetail(movie.id)">购票</button>
              </div>
            </article>
          </div>
        </div>

        <div class="sub-section">
          <div class="section-head">
            <h2>即将上映</h2>
            <span class="section-sub">提前锁定想看</span>
          </div>
          <div class="cards upcoming-cards">
            <article
              v-for="movie in upcomingMovies"
              :key="movie.id"
              class="card"
              @click="goDetail(movie.id)"
            >
              <div class="poster" :style="{ backgroundImage: `url(${movie.posterUrl})` }">
                <span class="rating">想看</span>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ movie.title }}</h3>
                <div class="tags">
                  <span v-for="tag in formatTags(movie.genre)" :key="tag" class="tag">{{ tag }}</span>
                </div>
                <div class="card-meta">
                  <span class="release">上映 {{ formatDate(movie.releaseDate) }}</span>
                </div>
                <button
                  class="wish"
                  :class="{ active: isFavorited(movie.id) }"
                  type="button"
                  @click.stop="handleToggleFavorite(movie.id)"
                >
                  {{ isFavorited(movie.id) ? '已想看' : '想看' }}
                </button>
              </div>
            </article>
          </div>
        </div>
      </div>

      <aside class="sidebar">
        <div class="rank-block">
          <div class="rank-head">
            <h3>高分排行</h3>
            <span class="rank-tag">Top 10</span>
          </div>
          <ol class="rank-list">
            <li
              v-for="(item, index) in dynamicRanking"
              :key="item.id"
              class="rank-item"
              :class="{ highlight: index === 0 }"
              @click="goDetail(item.id)"
            >
              <span class="rank-no" :class="{ top: index < 3 }">{{ index + 1 }}</span>
              <div class="rank-info">
                <p class="rank-title">{{ item.title }}</p>
                <p class="rank-amount">{{ item.rating }} 分</p>
              </div>
              <img
                v-if="index === 0 && item.posterUrl"
                :src="item.posterUrl"
                alt="海报"
                class="rank-thumb"
              />
            </li>
          </ol>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { StarFilled } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { 
  getHotMoviesApi, 
  getUpcomingMoviesApi, 
  getMoviesByCategoryApi,
  getMovieRankingApi,
  toggleFavoriteApi,
  getUserFavoritesApi
} from '../api/movie';
import { useUserStore } from '../store/userInfo';

interface Movie {
  id: number;
  title: string;
  rating: number;
  genre: string;
  posterUrl: string;
  synopsis: string;
  durationMinutes: number;
  releaseDate: string;
}

const router = useRouter();
const userStore = useUserStore();

const hotMovies = ref<Movie[]>([]);
const upcomingMovies = ref<Movie[]>([]);
const heroSlides = ref<Movie[]>([]);
const dynamicRanking = ref<Movie[]>([]);
const favoriteIds = ref<number[]>([]);

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

const loadData = async () => {
  try {
    const hotRes: any = await getHotMoviesApi();
    hotMovies.value = hotRes?.content || [];
    heroSlides.value = hotMovies.value.slice(0, 3);

    const soonRes: any = await getUpcomingMoviesApi();
    upcomingMovies.value = soonRes?.content || [];

    const rankRes: any = await getMovieRankingApi();
    dynamicRanking.value = rankRes || [];

    // 优先从 store 获取已有的 selected 列表，如果没有则从后端拉取
    if (userStore.state.id) {
      if (userStore.state.selected && userStore.state.selected.length > 0) {
        favoriteIds.value = userStore.state.selected;
      } else {
        const favRes: any = await getUserFavoritesApi(Number(userStore.state.id));
        favoriteIds.value = favRes || [];
        userStore.setUserInfo({ selected: favoriteIds.value });
      }
    }
  } catch (error) {
    console.error('Failed to load movies:', error);
  }
};

const handleCategoryClick = async (value: string, label: string) => {
  activeCategory.value = value;
  
  try {
    if (value === 'all') {
      await loadData();
    } else if (value === 'now_playing') {
      const res: any = await getHotMoviesApi();
      hotMovies.value = res?.content || [];
      upcomingMovies.value = [];
    } else if (value === 'coming_soon') {
      const res: any = await getUpcomingMoviesApi();
      upcomingMovies.value = soonRes?.content || [];
      hotMovies.value = [];
    } else {
      const res: any = await getMoviesByCategoryApi(label);
      hotMovies.value = res?.content || [];
      upcomingMovies.value = [];
    }
  } catch (error) {
    console.error('Filter category failed:', error);
  }
};

const handleToggleFavorite = async (movieId: number) => {
  if (!userStore.state.id) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  try {
    const res: any = await toggleFavoriteApi(Number(userStore.state.id), movieId);
    // 后端现在返回的是更新后的完整 selected 列表
    favoriteIds.value = res || [];
    // 同步到 store
    userStore.setUserInfo({ selected: favoriteIds.value });
    
    if (favoriteIds.value.includes(movieId)) {
      ElMessage.success('已加入收藏');
    } else {
      ElMessage.success('已取消收藏');
    }
  } catch (error) {
    console.error('Toggle favorite failed:', error);
    ElMessage.error('操作失败');
  }
};

const isFavorited = (movieId: number) => {
  return favoriteIds.value.includes(movieId);
};

const goDetail = (id: number) => {
  router.push(`/movieDetail/${id}`);
};

const formatTags = (genre: string) => {
  if (!genre) return [];
  return genre.split(',');
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN');
};

onMounted(() => {
  loadData();
});
</script>

<style lang="scss" scoped>
.home-page {
  background: #f7f8fb;
  min-height: 100vh;
  padding: 0 0 108px;
}

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

.chips {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.chip {
  padding: 8px 20px;
  border-radius: 20px;
  border: 1px solid #e5e7eb;
  background: #fff;
  color: #4b5563;
  cursor: pointer;
  white-space: nowrap;
  &.active { background: #15b8a6; color: #fff; border-color: #15b8a6; }
}

.sub-section { margin-bottom: 48px; }

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  cursor: pointer;
  transition: all 0.3s;
  &:hover { transform: translateY(-8px); box-shadow: 0 12px 24px rgba(0,0,0,0.1); }
}

.poster {
  height: 280px;
  background-size: cover;
  background-position: center;
  position: relative;
  .rating {
    position: absolute; top: 10px; left: 10px;
    background: rgba(0,0,0,0.7); color: #ffde6a;
    padding: 4px 8px; border-radius: 4px; font-weight: 700; font-size: 12px;
  }
}

.favorite-star {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 32px;
  height: 32px;
  background: rgba(0,0,0,0.4);
  backdrop-filter: blur(4px);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 2;
  border: 1px solid rgba(255, 255, 255, 0.2);

  .el-icon {
    font-size: 18px;
  }

  &:hover {
    background: rgba(0,0,0,0.6);
    transform: scale(1.1);
  }

  &.active {
    color: #ffc600;
    background: rgba(255, 198, 0, 0.15);
    border-color: rgba(255, 198, 0, 0.4);
    box-shadow: 0 0 12px rgba(255, 198, 0, 0.2);
    filter: drop-shadow(0 0 2px rgba(255, 198, 0, 0.5));
  }
}

.card-body {
  padding: 12px;
  .card-title { font-size: 16px; font-weight: 700; color: #111827; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  .card-subtitle { font-size: 12px; color: #6b7280; margin-bottom: 8px; }
}

.tags {
  display: flex; gap: 4px; flex-wrap: wrap; margin-bottom: 12px;
  .tag { background: #f3f4f6; color: #4b5563; font-size: 10px; padding: 2px 4px; border-radius: 4px; }
}

.buy, .wish {
  width: 100%; padding: 8px; border-radius: 6px; font-weight: 700; cursor: pointer; font-size: 13px;
}

.buy { background: #15b8a6; color: #fff; border: none; }
.wish { background: #fff; border: 1px solid #f97316; color: #f97316; }
.wish.active { background: #f97316; color: #fff; border-color: #f97316; }

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: sticky;
  top: 88px; /* 留出顶部导航栏的高度 + 间距 */
  align-self: start; /* 确保 sticky 生效，防止被拉伸到与左侧同高 */
}

.rank-block {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.rank-head {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;
  h3 { font-size: 18px; font-weight: 700; margin: 0; }
  .rank-tag { font-size: 12px; color: #15b8a6; background: #e9f7f6; padding: 2px 8px; border-radius: 10px; }
}

.rank-list {
  list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 12px;
}

.rank-item {
  display: flex; align-items: center; gap: 12px; padding: 8px; border-radius: 8px; transition: all 0.2s;
  cursor: pointer;
  &:hover { background: #f9fafb; }
  &.highlight { background: #f0fdfa; }
}

.rank-no {
  width: 24px; height: 24px; display: flex; align-items: center; justify-content: center;
  font-weight: 700; font-size: 14px; color: #9ca3af;
  &.top { color: #f97316; }
}

.rank-info { flex: 1; .rank-title { font-size: 14px; font-weight: 600; margin: 0; } .rank-amount { font-size: 12px; color: #6b7280; margin: 0; } }
.rank-thumb { width: 60px; height: 40px; border-radius: 4px; object-fit: cover; }
</style>
