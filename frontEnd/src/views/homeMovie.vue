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

    <section class="section">
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
    </section>

    <section class="section">
      <div class="section-head">
        <h2>精选影片</h2>
      </div>
      <div
        class="cards"
        v-infinite-scroll="loadMore"
        :infinite-scroll-disabled="noMore"
        infinite-scroll-distance="160"
      >
        <article
          v-for="movie in visibleMovies"
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
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { onMounted, computed } from 'vue';
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

// Tag codes -> 中文释义
// action 动作, comedy 喜剧, romance 爱情, animation 动画, scifi 科幻,
// adventure 冒险, drama 剧情, crime 犯罪, fantasy 奇幻, history 历史,
// thriller 惊悚, family 合家欢, racing 竞速, disaster 灾难, social 社会, workplace 职场

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
//选中类型
const activeCategory = ref(categories[0].value);
const PAGE_SIZE = 8;

const allMovies: Movie[] = [
  {
    id: 1,
    title: '奥本海默',
    subtitle: '克里斯托弗·诺兰 · 惊悚',
    rating: '9.2',
    tags: ['drama', 'history'], // 剧情, 历史
    imgUrl: 'https://image.tmdb.org/t/p/w500/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg',
    liked: false,
    category: 'now_playing',
  },
  {
    id: 2,
    title: '芭比',
    subtitle: '格蕾塔·葛韦格 · 喜剧',
    rating: '8.7',
    tags: ['comedy', 'fantasy'], // 喜剧, 奇幻
    imgUrl: 'https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg',
    liked: false,
    category: 'now_playing',
  },
  {
    id: 3,
    title: '蜘蛛侠：纵横宇宙',
    subtitle: '动画 · 冒险',
    rating: '9.0',
    tags: ['animation', 'adventure'], // 动画, 冒险
    imgUrl: 'https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg',
    liked: false,
    category: 'animation',
  },
  {
    id: 4,
    title: '疾速追杀4',
    subtitle: '基努·里维斯 · 动作',
    rating: '8.5',
    tags: ['action', 'crime'], // 动作, 犯罪
    imgUrl: 'https://image.tmdb.org/t/p/w500/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg',
    liked: false,
    category: 'action',
  },
  {
    id: 5,
    title: '火星救援特别版',
    subtitle: '科幻 · 冒险',
    rating: '8.9',
    tags: ['scifi', 'adventure'], // 科幻, 冒险
    imgUrl: 'https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg',
    liked: false,
    category: 'scifi',
  },
  {
    id: 6,
    title: '速度与激情：燃烧',
    subtitle: '赛车 · 动作',
    rating: '7.8',
    tags: ['action', 'racing'], // 动作, 竞速
    imgUrl: 'https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg',
    liked: false,
    category: 'action',
  },
  {
    id: 7,
    title: '深海奇缘',
    subtitle: '奇幻 · 冒险',
    rating: '8.3',
    tags: ['fantasy', 'adventure'], // 奇幻, 冒险
    imgUrl: 'https://image.tmdb.org/t/p/w500/aE5DYteFms4VTvk3gqyNwDqvF6k.jpg',
    liked: false,
    category: 'classic',
  },
  {
    id: 8,
    title: '沙丘·觉醒',
    subtitle: '科幻 · 史诗',
    rating: '8.5',
    tags: ['scifi', 'adventure'], // 科幻, 冒险
    imgUrl: 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0mva0y9Iv5V9V10D.jpg',
    liked: false,
    category: 'scifi',
  },
  {
    id: 9,
    title: '瞬息全宇宙',
    subtitle: '荒诞 · 家庭 · 动作',
    rating: '8.8',
    tags: ['fantasy', 'comedy'], // 奇幻, 喜剧
    imgUrl: 'https://image.tmdb.org/t/p/w500/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg',
    liked: false,
    category: 'comedy',
  },
  {
    id: 10,
    title: '流浪地球2',
    subtitle: '科幻 · 末世',
    rating: '8.4',
    tags: ['scifi', 'disaster'], // 科幻, 灾难
    imgUrl: 'https://image.tmdb.org/t/p/w500/5YZbUmjbMa3ClvSW1Wj3D6XGolb.jpg',
    liked: false,
    category: 'scifi',
  },
  {
    id: 11,
    title: '你的名字',
    subtitle: '新海诚 · 爱情 · 动画',
    rating: '9.1',
    tags: ['romance', 'animation'], // 爱情, 动画
    imgUrl: 'https://image.tmdb.org/t/p/w500/q719jXXEzOoYaps6babgKnONONX.jpg',
    liked: false,
    category: 'romance',
  },
  {
    id: 12,
    title: '小丑',
    subtitle: '心理 · 犯罪',
    rating: '8.9',
    tags: ['drama', 'crime'], // 剧情, 犯罪
    imgUrl: 'https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg',
    liked: false,
    category: 'classic',
  },
  {
    id: 13,
    title: '复仇者联盟：终局之战',
    subtitle: '漫威 · 超级英雄',
    rating: '8.6',
    tags: ['action', 'scifi'], // 动作, 科幻
    imgUrl: 'https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg',
    liked: false,
    category: 'action',
  },
  {
    id: 14,
    title: '寄生虫',
    subtitle: '奉俊昊 · 黑色幽默',
    rating: '9.0',
    tags: ['drama', 'thriller'], // 剧情, 惊悚
    imgUrl: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
    liked: false,
    category: 'classic',
  },
  {
    id: 15,
    title: '神偷奶爸4',
    subtitle: '合家欢 · 动画',
    rating: '8.1',
    tags: ['animation', 'family'], // 动画, 合家欢
    imgUrl: 'https://image.tmdb.org/t/p/w500/zfE0R94v1E8cuKAerbskfD3VfUt.jpg',
    liked: false,
    category: 'animation',
  },
  {
    id: 16,
    title: '壮志凌云2：独行侠',
    subtitle: '汤姆·克鲁斯 · 飞行',
    rating: '8.7',
    tags: ['action', 'drama'], // 动作, 剧情
    imgUrl: 'https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg',
    liked: false,
    category: 'action',
  },
  {
    id: 17,
    title: '别告诉她',
    subtitle: '卢璐王 · 家庭',
    rating: '8.2',
    tags: ['drama', 'comedy'], // 剧情, 喜剧
    imgUrl: 'https://image.tmdb.org/t/p/w500/zfhrUjk1oLNa68GdRutCKeIAAnw.jpg',
    liked: false,
    category: 'classic',
  },
  {
    id: 18,
    title: '夏洛特烦恼',
    subtitle: '开心麻花 · 喜剧',
    rating: '8.0',
    tags: ['comedy', 'romance'], // 喜剧, 爱情
    imgUrl: 'https://image.tmdb.org/t/p/w500/yY76zq9XSuJ4nWyPDuwkdV7Wt0c.jpg',
    liked: false,
    category: 'comedy',
  },
  {
    id: 19,
    title: '海王2',
    subtitle: 'DC · 水下冒险',
    rating: '7.6',
    tags: ['action', 'adventure'], // 动作, 冒险
    imgUrl: 'https://image.tmdb.org/t/p/w500/aqhLeieyTpTUKPOfZ3jzo2La0Mq.jpg',
    liked: false,
    category: 'scifi',
  },
  {
    id: 20,
    title: '黑寡妇',
    subtitle: '漫威 · 间谍',
    rating: '7.8',
    tags: ['action', 'thriller'], // 动作, 惊悚
    imgUrl: 'https://image.tmdb.org/t/p/w500/yDHYTfA3R0jFYba16jBB1ef8oIt.jpg',
    liked: false,
    category: 'action',
  },
  {
    id: 21,
    title: '年会不能停！',
    subtitle: '职场 · 喜剧',
    rating: '8.3',
    tags: ['comedy', 'workplace'], // 喜剧, 职场
    imgUrl: 'https://image.tmdb.org/t/p/w500/qik0hHDKtX0XZn3VOXnwvgkReC7.jpg',
    liked: false,
    category: 'comedy',
  },
  {
    id: 22,
    title: '我不是药神',
    subtitle: '现实 · 温情',
    rating: '9.0',
    tags: ['drama', 'social'], // 剧情, 社会
    imgUrl: 'https://image.tmdb.org/t/p/w500/84XzNL6vQGtY2C7shz5jcBEM2Js.jpg',
    liked: false,
    category: 'classic',
  },
];

const goDetail = () => {
  router.push('/homeMovie');
};

const toggleLike = (movie: Movie, e: Event) => {
  e.stopPropagation();
  movie.liked = !movie.liked;
};

const filteredPool = computed(() =>
  allMovies.filter((m) => activeCategory.value === 'all' || m.category === activeCategory.value),
);

const visibleMovies = ref<Movie[]>([]);
const page = ref(0);
const noMore = computed(() => visibleMovies.value.length >= filteredPool.value.length);

const loadMore = () => {
  const start = page.value * PAGE_SIZE;
  const slice = filteredPool.value.slice(start, start + PAGE_SIZE);
  if (slice.length) {
    visibleMovies.value.push(...slice);
    page.value += 1;
  }
};

const resetAndLoad = () => {
  page.value = 0;
  visibleMovies.value = [];
  loadMore();
};

const handleCategoryClick = (cat: string) => {
  activeCategory.value = cat;
  resetAndLoad();
};

onMounted( async () => {
  resetAndLoad();
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
  max-width: 1280px;
  margin: 0 auto 40px;
  padding: 0 24px;
}

.section-head {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}

.section-head h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #111827;
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
  gap: 22px;
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

.buy {
  width: 100%;
  height: 42px;
  border: none;
  border-radius: 10px;
  background: #15b8a6;
  color: #ffffff;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s ease, transform 0.15s ease;
}

.buy:hover {
  background: #129c90;
  transform: translateY(-1px);
}

</style>
