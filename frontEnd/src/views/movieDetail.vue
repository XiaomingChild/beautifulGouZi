<template>
  <div class="movie-detail">
    <!-- 头部横幅区 -->
    <section class="banner" :style="{ backgroundImage: `url(${movie.bannerUrl})` }">
      <div class="banner-mask"></div>
      <div class="banner-content content-wrapper">
        <div class="poster">
          <img :src="movie.imgUrl" :alt="movie.title" />
        </div>
        <div class="info">
          <h1 class="title">{{ movie.title }}</h1>
          <p class="en-title">{{ movie.enName }}</p>
          <div class="tags">
            <span v-for="tag in movie.tags" :key="tag" class="tag">{{ tag }}</span>
          </div>
          <p class="meta">{{ movie.duration }}分钟 / {{ movie.releaseDate }} 上映</p>
          <div class="stats">
            <div class="stat-item">
              <span class="label">评分</span>
              <span class="value accent">{{ movie.rating }}</span>
            </div>
          </div>
          <div class="actions">
            <button class="btn wish" @click="toggleWish">
              <el-icon><Star /></el-icon>
              {{ isWished ? '已想看' : '想看' }}
            </button>
            <button class="btn buy" @click="scrollToCinemas">立即购票</button>
          </div>
        </div>
      </div>
    </section>

    <!-- 主体内容区 -->
    <main class="main content-wrapper">
      <div class="left-col">
        <!-- 剧情简介 -->
        <section class="section">
          <h2 class="section-title">剧情简介</h2>
          <p class="description">{{ movie.description }}</p>
        </section>

        <!-- 演职员表 -->
        <section class="section">
          <h2 class="section-title">演职员表</h2>
          <div class="cast-list">
            <div v-for="person in movie.cast" :key="person.name" class="cast-item">
              <div class="avatar">
                <img :src="person.avatar || defaultAvatar" :alt="person.name" />
              </div>
              <p class="name">{{ person.name }}</p>
              <p class="role">{{ person.role }}</p>
            </div>
          </div>
        </section>

        <!-- 影院列表 -->
        <section id="cinemas" class="section">
          <h2 class="section-title">影院场次</h2>
          <div class="cinema-list">
            <div v-for="cinema in cinemaList" :key="cinema.id" class="cinema-item">
              <div class="cinema-info">
                <h3 class="name">{{ cinema.name }}</h3>
                <p class="address">{{ cinema.address }}</p>
                <div class="tags">
                  <span v-for="tag in cinema.tags" :key="tag" class="c-tag">{{ tag }}</span>
                </div>
              </div>
              <div class="cinema-actions">
                <div class="price-from">
                  <span class="symbol">￥</span>
                  <span class="num">39.9</span>
                  <span class="unit">起</span>
                </div>
                <button class="btn-select" @click="showSchedules(cinema.id)">选座购票</button>
              </div>

              <!-- 展开场次列表 -->
              <div v-if="activeCinemaId === cinema.id" class="schedule-table">
                <div v-for="sch in getSchedules(cinema.id)" :key="sch.id" class="sch-row">
                  <div class="time">
                    <p class="start">{{ sch.startTime }}</p>
                    <p class="end">{{ sch.endTime }} 散场</p>
                  </div>
                  <div class="type">
                    <p class="lang">{{ sch.type }}</p>
                    <p class="hall">{{ sch.hall }}</p>
                  </div>
                  <div class="price">
                    <span class="num">￥{{ sch.price }}</span>
                  </div>
                  <div class="buy-btn">
                    <button @click="goBooking(sch.id)">选座</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- 右侧边栏：热播电影/票房排行（可复用 Home 的逻辑） -->
      <aside class="right-col">
        <div class="sidebar-block">
          <h3 class="block-title">相关推荐</h3>
          <!-- 这里可以放几张推荐卡片 -->
        </div>
      </aside>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Star } from '@element-plus/icons-vue';
import { movies, cinemas, schedules } from '../mock/data';

const router = useRouter();
const route = useRoute();

const defaultAvatar = 'https://p0.meituan.net/movie/7e4334033ce546f332612a8385750f7510702.png';
const movie = ref(movies[0]); // 实际应根据 route.params.id 过滤
const cinemaList = ref(cinemas);
const activeCinemaId = ref<number | null>(null);
const isWished = ref(false);

const toggleWish = () => {
  isWished.value = !isWished.value;
};

const scrollToCinemas = () => {
  document.getElementById('cinemas')?.scrollIntoView({ behavior: 'smooth' });
};

const showSchedules = (id: number) => {
  activeCinemaId.value = activeCinemaId.value === id ? null : id;
};

const getSchedules = (cinemaId: number) => {
  return schedules.filter(s => s.cinemaId === cinemaId && s.movieId === movie.value.id);
};

const goBooking = (scheduleId: number) => {
  router.push({ path: '/seatSelection', query: { id: scheduleId } });
};

onMounted(() => {
  // 根据 ID 获取电影逻辑...
});
</script>

<style lang="scss" scoped>
.movie-detail {
  background: #f7f8fb;
  min-height: 100vh;
  padding-bottom: 60px;

  .content-wrapper {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .banner {
    height: 400px;
    background-size: cover;
    background-position: center;
    position: relative;
    display: flex;
    align-items: center;

    &-mask {
      position: absolute;
      inset: 0;
      background: linear-gradient(to right, rgba(0,0,0,0.8) 20%, rgba(0,0,0,0.4) 100%);
      backdrop-filter: blur(20px);
    }

    &-content {
      position: relative;
      z-index: 1;
      display: flex;
      gap: 40px;
      color: #fff;

      .poster {
        width: 240px;
        height: 330px;
        border: 4px solid #fff;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 10px 30px rgba(0,0,0,0.5);
        img { width: 100%; height: 100%; object-fit: cover; }
      }

      .info {
        flex: 1;
        padding-top: 20px;
        .title { font-size: 32px; margin-bottom: 8px; }
        .en-title { font-size: 16px; opacity: 0.7; margin-bottom: 16px; }
        .tags { display: flex; gap: 8px; margin-bottom: 16px;
          .tag { background: rgba(255,255,255,0.2); padding: 4px 12px; border-radius: 4px; font-size: 12px; }
        }
        .meta { font-size: 14px; opacity: 0.8; margin-bottom: 24px; }

        .stats {
          display: flex; gap: 40px; margin-bottom: 30px;
          .stat-item {
            display: flex; flex-direction: column; gap: 4px;
            .label { font-size: 12px; opacity: 0.6; }
            .value { font-size: 24px; font-weight: bold; }
            .accent { color: #f97316; }
          }
        }

        .actions {
          display: flex; gap: 16px;
          .btn {
            height: 44px; padding: 0 30px; border-radius: 8px; cursor: pointer; border: none; font-weight: 600;
            display: flex; align-items: center; gap: 8px; transition: all 0.3s;
            &.wish { background: rgba(255,255,255,0.15); color: #fff; border: 1px solid rgba(255,255,255,0.3); }
            &.buy { background: #15b8a6; color: #fff; box-shadow: 0 8px 16px rgba(21, 184, 166, 0.3); }
            &:hover { transform: translateY(-2px); opacity: 0.9; }
          }
        }
      }
    }
  }

  .main {
    margin-top: 40px;
    display: grid;
    grid-template-columns: 1fr 300px;
    gap: 40px;

    .section {
      background: #fff;
      padding: 24px;
      border-radius: 12px;
      margin-bottom: 24px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.03);

      &-title {
        font-size: 20px; font-weight: 700; margin-bottom: 20px;
        padding-left: 12px; border-left: 4px solid #15b8a6;
      }

      .description { line-height: 1.8; color: #4b5563; }

      .cast-list {
        display: flex; gap: 20px; overflow-x: auto; padding-bottom: 10px;
        &::-webkit-scrollbar { height: 4px; }
        &::-webkit-scrollbar-thumb { background: #e5e7eb; border-radius: 2px; }

        .cast-item {
          width: 100px; flex-shrink: 0; text-align: center;
          .avatar {
            width: 100px; height: 140px; border-radius: 6px; overflow: hidden; margin-bottom: 8px;
            img { width: 100%; height: 100%; object-fit: cover; }
          }
          .name { font-size: 13px; font-weight: 600; margin-bottom: 2px; }
          .role { font-size: 12px; color: #9ca3af; }
        }
      }

      .cinema-list {
        display: flex; flex-direction: column; gap: 16px;
        .cinema-item {
          border-bottom: 1px solid #f1f3f6; padding-bottom: 16px;
          &:last-child { border-bottom: none; }

          .cinema-info {
            .name { font-size: 18px; margin-bottom: 6px; }
            .address { font-size: 13px; color: #6b7280; margin-bottom: 8px; }
            .tags { display: flex; gap: 6px;
              .c-tag { border: 1px solid #15b8a6; color: #15b8a6; font-size: 11px; padding: 2px 6px; border-radius: 4px; }
            }
          }

          .cinema-actions {
            display: flex; justify-content: space-between; align-items: center; margin-top: 12px;
            .price-from {
              color: #f97316;
              .symbol { font-size: 12px; }
              .num { font-size: 22px; font-weight: bold; }
              .unit { font-size: 12px; color: #9ca3af; margin-left: 2px; }
            }
            .btn-select {
              background: #15b8a6; color: #fff; border: none; padding: 8px 20px; border-radius: 20px; cursor: pointer;
            }
          }

          .schedule-table {
            margin-top: 20px; background: #f9fafb; border-radius: 8px; overflow: hidden;
            .sch-row {
              display: grid; grid-template-columns: 120px 1fr 100px 100px; align-items: center;
              padding: 15px; border-bottom: 1px solid #eee;
              &:last-child { border-bottom: none; }

              .time {
                .start { font-size: 18px; font-weight: bold; }
                .end { font-size: 12px; color: #9ca3af; }
              }
              .type {
                .lang { font-size: 14px; }
                .hall { font-size: 12px; color: #9ca3af; }
              }
              .price { color: #f97316; font-weight: bold; }
              .buy-btn button {
                background: #f97316; color: #fff; border: none; padding: 5px 15px; border-radius: 15px; cursor: pointer;
              }
            }
          }
        }
      }
    }
  }
}
</style>