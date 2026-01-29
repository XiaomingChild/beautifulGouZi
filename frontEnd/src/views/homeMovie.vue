<template>
  <main class="home-page">
    <section class="hero">
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
          <div class="hero-media" :style="{ backgroundImage: `url(${item.imgUrl})` }">
            <div class="hero-mask"></div>
            <div class="hero-content">
              <p class="hero-tag">特别放映</p>
              <h1 class="hero-title">《{{ item.title }}》特别放映</h1>
              <p class="hero-subtitle">{{ item.desc }}</p>
              <div class="hero-actions">
                <button class="cta" type="button" @click="goDetail">立即购票</button>
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
            @click="handleCategoryClick(item.value)"
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
              v-for="movie in limitedNowPlaying"
              :key="movie.id"
              class="card"
              @click="goDetail"
            >
              <div class="poster" :style="{ backgroundImage: `url(${movie.imgUrl})` }">
                <span class="rating">{{ movie.rating }}</span>
                <button
                  class="fav"
                  :class="{ active: movie.liked }"
                  type="button"
                  aria-label="收藏"
                  @click="toggleLike(movie, $event)"
                >
                  <svg viewBox="0 0 24 24" aria-hidden="true">
                    <path
                      d="M12 17.27 6.18 21l1.6-6.86L2 9.63l7-.6L12 2l3 7.03 7 .6-5.78 4.51L17.82 21z"
                    />
                  </svg>
                </button>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ movie.title }}</h3>
                <p class="card-subtitle">{{ movie.subtitle }}</p>
                <div class="tags">
                  <span v-for="tag in movie.tags" :key="tag" class="tag">{{ tag }}</span>
                </div>
                <button class="buy" type="button" @click.stop="goDetail">购票</button>
              </div>
            </article>
          </div>
        </div>

        <div class="sub-section">
          <div class="section-head">
            <h2>即将上线</h2>
            <span class="section-sub">提前锁定想看</span>
          </div>
          <div class="cards upcoming-cards">
            <article
              v-for="movie in limitedComingSoon"
              :key="movie.id"
              class="card"
              @click="goDetail"
            >
              <div class="poster" :style="{ backgroundImage: `url(${movie.imgUrl})` }">
                <span class="rating">{{ movie.rating }}</span>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ movie.title }}</h3>
                <p class="card-subtitle">{{ movie.subtitle }}</p>
                <div class="tags">
                  <span v-for="tag in movie.tags" :key="tag" class="tag">{{ tag }}</span>
                </div>
                <div class="card-meta">
                  <span class="release">上映 {{ movie.releaseDate }}</span>
                  <span class="wish-count">{{ formatNumber(movie.wishes) }} 人想看</span>
                </div>
                <button
                  class="wish"
                  :class="{ active: movie.want }"
                  type="button"
                  @click.stop="toggleWish(movie, $event)"
                >
                  {{ movie.want ? '已想看' : '想看' }}
                </button>
              </div>
            </article>
          </div>
        </div>
      </div>

      <aside class="sidebar">
        <div class="rank-block">
          <div class="rank-head">
            <h3>票房排行</h3>
            <span class="rank-tag">今日</span>
          </div>
          <ol class="rank-list">
            <li
              v-for="(item, index) in boxOfficeRankings"
              :key="item.title"
              class="rank-item"
              :class="{ highlight: index === 0 }"
            >
              <span class="rank-no" :class="{ top: index < 3 }">{{ index + 1 }}</span>
              <div class="rank-info">
                <p class="rank-title">{{ item.title }}</p>
                <p class="rank-amount">{{ item.amount }}</p>
              </div>
              <img
                v-if="index === 0 && item.imgUrl"
                :src="item.imgUrl"
                alt="票房冠军海报"
                class="rank-thumb"
              />
            </li>
          </ol>
        </div>

        <div class="rank-block">
          <div class="rank-head">
            <h3>即将上映期待排行</h3>
            <span class="rank-tag">想看</span>
          </div>
          <div v-if="expectedTop" class="expected-top">
            <div class="expected-thumb" :style="{ backgroundImage: `url(${expectedTop.imgUrl})` }"></div>
            <div class="expected-info">
              <p class="expected-title">1. {{ expectedTop.title }}</p>
              <p class="expected-release">上映时间：{{ expectedTop.releaseDate }}</p>
              <p class="expected-wish">{{ formatNumber(expectedTop.wishes) }} 人想看</p>
            </div>
          </div>

          <div class="expected-grid" v-if="expectedGrid.length">
            <div
              v-for="(item, idx) in expectedGrid"
              :key="item.id"
              class="expected-card"
            >
              <div class="expected-card-thumb" :style="{ backgroundImage: `url(${item.imgUrl})` }">
                <span class="expected-badge">{{ idx + 2 }}</span>
              </div>
              <div class="expected-card-body">
                <p class="expected-card-title">{{ item.title }}</p>
                <p class="expected-card-wish">{{ formatNumber(item.wishes) }} 人想看</p>
              </div>
            </div>
          </div>

          <ol class="rank-list expected-list" v-if="expectedRest.length">
            <li
              v-for="(item, index) in expectedRest"
              :key="item.id"
              class="rank-item"
            >
              <span class="rank-no" :class="{ top: index + 3 < 3 }">{{ index + 4 }}</span>
              <div class="rank-info">
                <p class="rank-title">{{ item.title }}</p>
                <p class="rank-amount">{{ formatNumber(item.wishes) }} 人想看</p>
              </div>
            </li>
          </ol>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue';
import { useRouter } from 'vue-router';

interface Movie {
  id: number;
  title: string;
  subtitle: string;
  rating: string;
  tags: string[];
  imgUrl: string;
  liked: boolean;
  category: string;
  status: 'now' | 'soon';
  releaseDate?: string;
  wishes?: number;
  want?: boolean;
}

interface RankingItem {
  title: string;
  amount: string;
  imgUrl?: string;
}

const router = useRouter();

const heroSlides = [
  {
    id: 1,
    title: '星际穿越',
    desc: '诺兰经典科幻巨制，IMAX 体验震撼回归',
    imgUrl: 'https://image.tmdb.org/t/p/original/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg',
  },
  {
    id: 2,
    title: '星际探索',
    desc: '孤独宇航员的漫长旅程，寻找深空真相',
    imgUrl: 'https://image.tmdb.org/t/p/original/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg',
  },
  {
    id: 3,
    title: '太空营救',
    desc: '失联航天员紧急回家，火星轨道惊心动魄',
    imgUrl: 'https://image.tmdb.org/t/p/original/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg',
  },
];

const categories = [
  { label: '全部', value: 'all' },
  { label: '正在热映', value: 'now_playing' },
  { label: '即将上映', value: 'coming_soon' },
  { label: '经典影片', value: 'classic' },
  { label: '动作片', value: 'action' },
  { label: '喜剧片', value: 'comedy' },
  { label: '爱情片', value: 'romance' },
  { label: '动漫片', value: 'animation' },
  { label: '科幻片', value: 'scifi' },
];

const activeCategory = ref(categories[0].value);

const allMovies = reactive<Movie[]>([
  {
    id: 1,
    title: '奥本海默',
    subtitle: '克里斯托弗·诺兰 · 惊悚',
    rating: '9.2',
    tags: ['drama', 'history'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg',
    liked: false,
    category: 'now_playing',
    status: 'now',
  },
  {
    id: 2,
    title: '芭比',
    subtitle: '格蕾塔·葛韦格 · 喜剧',
    rating: '8.7',
    tags: ['comedy', 'fantasy'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg',
    liked: false,
    category: 'comedy',
    status: 'now',
  },
  {
    id: 3,
    title: '蜘蛛侠：纵横宇宙',
    subtitle: '动画 · 冒险',
    rating: '9.0',
    tags: ['animation', 'adventure'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg',
    liked: false,
    category: 'animation',
    status: 'now',
  },
  {
    id: 4,
    title: '疾速追杀4',
    subtitle: '基努·里维斯 · 动作',
    rating: '8.5',
    tags: ['action', 'crime'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg',
    liked: false,
    category: 'action',
    status: 'now',
  },
  {
    id: 5,
    title: '火星救援特别版',
    subtitle: '科幻 · 冒险',
    rating: '8.9',
    tags: ['scifi', 'adventure'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg',
    liked: false,
    category: 'scifi',
    status: 'now',
  },
  {
    id: 6,
    title: '速度与激情：燃烧',
    subtitle: '赛车 · 动作',
    rating: '7.8',
    tags: ['action', 'racing'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg',
    liked: false,
    category: 'action',
    status: 'now',
  },
  {
    id: 7,
    title: '深海奇缘',
    subtitle: '奇幻 · 冒险',
    rating: '8.3',
    tags: ['fantasy', 'adventure'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/aE5DYteFms4VTvk3gqyNwDqvF6k.jpg',
    liked: false,
    category: 'classic',
    status: 'now',
  },
  {
    id: 8,
    title: '沙丘·觉醒',
    subtitle: '科幻 · 史诗',
    rating: '8.5',
    tags: ['scifi', 'adventure'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0mva0y9Iv5V9V10D.jpg',
    liked: false,
    category: 'scifi',
    status: 'now',
  },
  {
    id: 9,
    title: '瞬息全宇宙',
    subtitle: '荒诞 · 家庭 · 动作',
    rating: '8.8',
    tags: ['fantasy', 'comedy'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg',
    liked: false,
    category: 'comedy',
    status: 'now',
  },
  {
    id: 10,
    title: '流浪地球2',
    subtitle: '科幻 · 末世',
    rating: '8.4',
    tags: ['scifi', 'disaster'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/5YZbUmjbMa3ClvSW1Wj3D6XGolb.jpg',
    liked: false,
    category: 'scifi',
    status: 'now',
  },
  {
    id: 11,
    title: '你的名字',
    subtitle: '新海诚 · 爱情 · 动画',
    rating: '9.1',
    tags: ['romance', 'animation'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/q719jXXEzOoYaps6babgKnONONX.jpg',
    liked: false,
    category: 'romance',
    status: 'now',
  },
  {
    id: 12,
    title: '小丑',
    subtitle: '心理 · 犯罪',
    rating: '8.9',
    tags: ['drama', 'crime'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg',
    liked: false,
    category: 'classic',
    status: 'now',
  },
  {
    id: 101,
    title: '疯狂动物城2',
    subtitle: '动画 · 喜剧',
    rating: '想看',
    tags: ['animation', 'comedy'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/ydcXtH3UX0DpDhmPEw24kTx5cRQ.jpg',
    liked: false,
    category: 'coming_soon',
    status: 'soon',
    releaseDate: '2025-02-14',
    wishes: 182430,
    want: false,
  },
  {
    id: 102,
    title: '哪吒2：魔童闹海',
    subtitle: '国漫 · 奇幻',
    rating: '想看',
    tags: ['animation', 'fantasy'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/4W0FnjSGn4x0mKZlBRx8OjFxQUM.jpg',
    liked: false,
    category: 'coming_soon',
    status: 'soon',
    releaseDate: '2025-02-10',
    wishes: 234560,
    want: false,
  },
  {
    id: 103,
    title: '流浪地球3',
    subtitle: '刘慈欣 · 科幻',
    rating: '想看',
    tags: ['scifi', 'adventure'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0mva0y9Iv5V9V10D.jpg',
    liked: false,
    category: 'scifi',
    status: 'soon',
    releaseDate: '2025-03-15',
    wishes: 210340,
    want: false,
  },
  {
    id: 104,
    title: '芭比2',
    subtitle: '梦幻续作',
    rating: '想看',
    tags: ['comedy', 'fantasy'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg',
    liked: false,
    category: 'comedy',
    status: 'soon',
    releaseDate: '2025-05-01',
    wishes: 165500,
    want: false,
  },
  {
    id: 105,
    title: '你的名字2',
    subtitle: '新海诚 · 青春',
    rating: '想看',
    tags: ['romance', 'animation'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/q719jXXEzOoYaps6babgKnONONX.jpg',
    liked: false,
    category: 'romance',
    status: 'soon',
    releaseDate: '2025-04-12',
    wishes: 145900,
    want: false,
  },
  {
    id: 106,
    title: '灌篮高手：全国大赛',
    subtitle: '热血 · 体育',
    rating: '想看',
    tags: ['animation', 'family'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/zfE0R94v1E8cuKAerbskfD3VfUt.jpg',
    liked: false,
    category: 'animation',
    status: 'soon',
    releaseDate: '2025-06-08',
    wishes: 132200,
    want: false,
  },
  {
    id: 107,
    title: '银河护卫队：重启',
    subtitle: '漫威 · 冒险',
    rating: '想看',
    tags: ['action', 'scifi'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg',
    liked: false,
    category: 'action',
    status: 'soon',
    releaseDate: '2025-07-05',
    wishes: 118900,
    want: false,
  },
  {
    id: 108,
    title: '神偷奶爸5',
    subtitle: '合家欢 · 搞笑',
    rating: '想看',
    tags: ['animation', 'family'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/zfE0R94v1E8cuKAerbskfD3VfUt.jpg',
    liked: false,
    category: 'animation',
    status: 'soon',
    releaseDate: '2025-07-20',
    wishes: 98000,
    want: false,
  },
  {
    id: 109,
    title: '黑客帝国：复兴',
    subtitle: '科幻 · 哲思',
    rating: '想看',
    tags: ['scifi', 'thriller'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/84XzNL6vQGtY2C7shz5jcBEM2Js.jpg',
    liked: false,
    category: 'scifi',
    status: 'soon',
    releaseDate: '2025-08-16',
    wishes: 87000,
    want: false,
  },
  {
    id: 110,
    title: '速度与激情11',
    subtitle: '赛车 · 动作',
    rating: '想看',
    tags: ['action', 'racing'],
    imgUrl: 'https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg',
    liked: false,
    category: 'action',
    status: 'soon',
    releaseDate: '2025-09-01',
    wishes: 240000,
    want: false,
  },
]);

const boxOfficeRankings: RankingItem[] = [
  {
    title: '疯狂动物城2',
    amount: '81.97万',
    imgUrl: 'https://image.tmdb.org/t/p/w500/ydcXtH3UX0DpDhmPEw24kTx5cRQ.jpg',
  },
  { title: '重返寂静岭', amount: '62.05万' },
  { title: '阿凡达3', amount: '55.29万' },
  { title: '匪杀', amount: '49.60万' },
  { title: '爆水管', amount: '40.21万' },
  { title: '火星救援特别版', amount: '38.45万' },
  { title: '奥本海默', amount: '36.12万' },
  { title: '芭比', amount: '32.24万' },
  { title: '瞬息全宇宙', amount: '29.50万' },
  { title: '速度与激情：燃烧', amount: '26.08万' },
];

const matchCategory = (movie: Movie, target: string) => {
  if (target === 'all') return true;
  if (target === 'now_playing') return movie.status === 'now';
  if (target === 'coming_soon') return movie.status === 'soon';
  return movie.category === target;
};

const filteredNowPlaying = computed(() =>
  allMovies.filter((m) => m.status === 'now' && matchCategory(m, activeCategory.value)),
);

const filteredComingSoon = computed(() =>
  allMovies.filter((m) => m.status === 'soon' && matchCategory(m, activeCategory.value)),
);

const limitedNowPlaying = computed(() => filteredNowPlaying.value.slice(0, 12));
const limitedComingSoon = computed(() => filteredComingSoon.value.slice(0, 12));

const expectedRanking = computed(() =>
  [...allMovies.filter((m) => m.status === 'soon')]
    .sort((a, b) => (b.wishes ?? 0) - (a.wishes ?? 0))
    .slice(0, 10),
);

const expectedTop = computed(() => expectedRanking.value[0] ?? null);
const expectedGrid = computed(() => expectedRanking.value.slice(1, 3));
const expectedRest = computed(() => expectedRanking.value.slice(3));

const handleCategoryClick = (cat: string) => {
  activeCategory.value = cat;
};

const goDetail = () => {
  router.push('/homeMovie');
};

const toggleLike = (movie: Movie, e: Event) => {
  e.stopPropagation();
  movie.liked = !movie.liked;
};

const toggleWish = (movie: Movie, e: Event) => {
  e.stopPropagation();
  if (movie.status !== 'soon') return;
  movie.want = !movie.want;
  const delta = movie.want ? 1 : -1;
  movie.wishes = Math.max(0, (movie.wishes ?? 0) + delta);
};

const formatNumber = (num: number | undefined) => (num ?? 0).toLocaleString('zh-CN');
</script>

<style lang="scss" scoped>
.home-page {
  background: #f7f8fb;
  min-height: 100vh;
  padding: 0 0 108px;
}

.hero {
  margin: 0 auto 36px;
  border-radius: 0;
  overflow: hidden;
  width: 100%;
}

:deep(.el-carousel__container) {
  border-radius: 0;
}

:deep(.el-carousel__arrow) {
  width: 46px;
  height: 46px;
  background: rgba(0, 0, 0, 0.38);
  border: 1px solid rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(8px);
}

:deep(.el-carousel__arrow:hover) {
  background: rgba(21, 184, 166, 0.86);
}

:deep(.el-carousel__indicators--outside) {
  margin-top: 12px;
}

:deep(.el-carousel__indicator button) {
  height: 4px;
  background: #d6d8dc;
  border-radius: 999px;
}

:deep(.el-carousel__indicator.is-active button) {
  background: #15b8a6;
}

.hero-item {
  height: 540px;
}
.hero-media {
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.hero-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(120deg, rgba(6, 46, 60, 0.78), rgba(6, 46, 60, 0.45) 55%, rgba(6, 46, 60, 0.55));
}

.hero-content {
  position: relative;
  z-index: 1;
  color: #ffffff;
  max-width: 520px;
  padding: 0 72px;
  display: grid;
  gap: 14px;
}

.hero-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 108px;
  height: 30px;
  background: rgba(255, 255, 255, 0.12);
  border-radius: 999px;
  font-size: 13px;
  letter-spacing: 1px;
}

.hero-title {
  font-size: 40px;
  font-weight: 800;
  margin: 0;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 15px;
  margin: 0;
  opacity: 0.94;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 6px;
}

.hero-dots {
  display: none;
}

.cta {
  min-width: 140px;
  height: 44px;
  border-radius: 10px;
  border: none;
  background: linear-gradient(130deg, #16c3b0, #14a191);
  color: #ffffff;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: transform 0.18s ease, box-shadow 0.18s ease;
  box-shadow: 0 14px 28px rgba(20, 160, 147, 0.28);
}

.cta:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 36px rgba(20, 160, 147, 0.32);
}

.section {
  max-width: 1400px;
  margin: 0 auto 32px;
  padding: 0 6px;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 28px;
  align-items: start;
}

.main-column {
  display: grid;
  gap: 22px;
}

.section-head {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-bottom: 12px;
}

.section-head h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #111827;
}

.section-sub {
  font-size: 13px;
  color: #6b7280;
}

.sub-section {
  display: grid;
  gap: 16px;
}

.chips {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.chip {
  min-width: 108px;
  height: 40px;
  padding: 0 18px;
  border: 1px solid #e3e6eb;
  border-radius: 10px;
  background: #ffffff;
  color: #4b5563;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.chip.active {
  background: #15b8a6;
  color: #ffffff;
  border-color: #15b8a6;
  box-shadow: 0 10px 18px rgba(21, 184, 166, 0.18);
}

.chip:hover {
  border-color: #b5dfe6;
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 18px;
}

.card {
  background: #ffffff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 8px 18px rgba(17, 24, 39, 0.08);
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(17, 24, 39, 0.14);
}

.poster {
  position: relative;
  aspect-ratio: 2 / 3;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  overflow: hidden;
}

.rating {
  position: absolute;
  top: 14px;
  left: 14px;
  padding: 7px 12px;
  border-radius: 14px;
  background: rgba(0, 0, 0, 0.66);
  color: #ffde6a;
  font-weight: 800;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(6px);
}

.fav {
  position: absolute;
  right: 14px;
  bottom: 14px;
  width: 42px;
  height: 42px;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.16);
  background: rgba(17, 24, 39, 0.55);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #f5d24f;
  transition: all 0.18s ease;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.16);
}

.fav svg {
  width: 20px;
  height: 20px;
  fill: transparent;
  stroke: currentColor;
  stroke-width: 1.6px;
}

.fav:hover {
  transform: translateY(-1px);
}

.fav.active {
  background: #ffffff;
  color: #f0b90b;
  box-shadow: 0 12px 22px rgba(0, 0, 0, 0.18);
}

.fav.active svg {
  fill: currentColor;
  stroke: currentColor;
}

.card-body {
  padding: 14px 16px 16px;
  display: grid;
  gap: 8px;
}

.card-title {
  margin: 0;
  font-size: 17px;
  color: #111827;
}

.card-subtitle {
  margin: 0;
  font-size: 14px;
  color: #6b7280;
}

.tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  background: #f3f4f6;
  color: #4b5563;
  font-size: 12px;
  border-radius: 6px;
  padding: 4px 8px;
}

.card-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
  color: #6b7280;
}

.release {
  color: #0f9a8a;
}

.wish-count {
  color: #f97316;
  font-weight: 700;
}

.buy,
.wish {
  width: 100%;
  height: 42px;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s ease, transform 0.15s ease, color 0.15s ease;
}

.buy {
  border: none;
  background: #15b8a6;
  color: #ffffff;
}

.buy:hover {
  background: #129c90;
  transform: translateY(-1px);
}

.wish {
  border: 1px solid #f97316;
  background: #fff7ed;
  color: #f97316;
}

.wish.active {
  background: #f97316;
  color: #ffffff;
}

.sidebar {
  position: sticky;
  top: 92px;
  display: grid;
  gap: 18px;
  height: fit-content;
}

.rank-block {
  background: #ffffff;
  border-radius: 14px;
  padding: 14px 14px 10px;
  box-shadow: 0 8px 22px rgba(17, 24, 39, 0.08);
}

.rank-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.rank-head h3 {
  margin: 0;
  font-size: 17px;
  color: #111827;
}

.rank-tag {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 999px;
  background: #e8f7f5;
  color: #0f9a8a;
}

.rank-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 8px;
}

.rank-item {
  display: grid;
  grid-template-columns: 32px 1fr auto;
  align-items: center;
  gap: 10px;
  padding: 10px 8px;
  border-radius: 10px;
  background: #f9fafb;
}

.rank-item.highlight {
  background: #ffffff;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
}

.rank-no {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  background: #e5e7eb;
  color: #111827;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
}

.rank-no.top {
  background: #ffe8d9;
  color: #ef3b2d;
}

.rank-info {
  display: grid;
  gap: 4px;
}

.rank-title {
  margin: 0;
  font-size: 14px;
  color: #111827;
}

.rank-amount {
  margin: 0;
  font-size: 13px;
  color: #f97316;
  font-weight: 700;
}

.rank-thumb {
  width: 88px;
  height: 54px;
  border-radius: 10px;
  object-fit: cover;
}

.expected-top {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 12px;
  padding: 10px;
  border-radius: 12px;
  background: #fff8f0;
  margin-bottom: 12px;
}

.expected-thumb {
  width: 120px;
  height: 150px;
  border-radius: 10px;
  background-size: cover;
  background-position: center;
}

.expected-info {
  display: grid;
  gap: 6px;
}

.expected-title {
  margin: 0;
  font-size: 15px;
  font-weight: 700;
  color: #111827;
}

.expected-release {
  margin: 0;
  font-size: 13px;
  color: #6b7280;
}

.expected-wish {
  margin: 0;
  font-size: 14px;
  color: #f59e0b;
  font-weight: 700;
}

.expected-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
  margin-bottom: 10px;
}

.expected-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.expected-card-thumb {
  position: relative;
  height: 120px;
  background-size: cover;
  background-position: center;
}

.expected-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  padding: 4px 8px;
  border-radius: 10px;
  background: #f97316;
  color: #ffffff;
  font-weight: 700;
  font-size: 12px;
}

.expected-card-body {
  padding: 10px 12px 12px;
  display: grid;
  gap: 4px;
}

.expected-card-title {
  margin: 0;
  font-size: 14px;
  color: #111827;
  font-weight: 700;
}

.expected-card-wish {
  margin: 0;
  font-size: 13px;
  color: #f59e0b;
  font-weight: 700;
}

.expected-list {
  margin-top: 4px;
}
</style>
