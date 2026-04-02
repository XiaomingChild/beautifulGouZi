<template>
  <div class="profile-page">
    <div class="profile-container content-wrapper">
      <!-- 左侧菜单 -->
      <aside class="profile-sidebar">
        <div class="user-brief">
          <div class="avatar-wrapper" @click="router.push('/avatar-select')">
            <img :src="user.avatar || defaultAvatar" alt="用户头像" />
            <div class="upload-mask">修改头像</div>
          </div>
          <h3 class="user-name">{{ user.nickname || user.account || '未登录' }}</h3>
        </div>
        
        <nav class="side-menu">
          <button 
            v-for="item in menuItems" 
            :key="item.id"
            class="menu-item"
            :class="{ active: activeTab === item.id }"
            @click="activeTab = item.id"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </button>
        </nav>
      </aside>

      <!-- 右侧内容 -->
      <main class="profile-content">
        <!-- 个人详情 -->
        <section v-if="activeTab === 'profile'" class="content-section">
          <h2 class="section-head">基本设置</h2>
          <div class="info-form">
            <div class="form-item">
              <label>用户账号</label>
              <el-input v-model="user.account" disabled />
            </div>
            <div class="form-item">
              <label>用户昵称</label>
              <el-input v-model="profileData.nickname" placeholder="请输入昵称" />
            </div>
            <div class="form-item">
              <label>联系方式</label>
              <el-input v-model="profileData.phone" placeholder="请输入手机号" />
            </div>
            <div class="form-item">
              <label>个性签名</label>
              <el-input v-model="profileData.bio" type="textarea" :rows="3" placeholder="介绍一下自己吧" />
            </div>
            <div class="form-actions">
              <button class="btn-save" :loading="saving" @click="saveProfile">保存修改</button>
            </div>
          </div>
        </section>

        <!-- 我的订单 -->
        <section v-if="activeTab === 'records'" class="content-section">
          <h2 class="section-head">我的订单</h2>
          <div v-if="orderList.length === 0" class="empty-state">
            <img src="https://p0.meituan.net/movie/60fd63e6e185f577f8841804f32650051234.png" alt="" />
            <p>您还没有购票记录，去选一部电影吧</p>
          </div>
          <div v-else class="order-list">
            <div v-for="order in orderList" :key="order.id" class="order-card">
              <div class="order-header">
                <span class="order-time">购票时间：{{ order.createTime }}</span>
                <span class="order-id">订单号：{{ order.id }}</span>
              </div>
              <div class="order-body">
                <div class="movie-info">
                  <div class="detail">
                    <h4 class="title">《{{ order.movieTitle }}》</h4>
                    <p class="cinema">{{ order.cinemaName }}</p>
                    <p class="hall-time">{{ order.hall }} | {{ order.showTime }}</p>
                    <div class="seats">
                      <span v-for="seat in order.seats" :key="seat" class="seat-tag">{{ seat }}</span>
                    </div>
                  </div>
                </div>
                <div class="order-status">
                  <div class="status-tag paid">已完成</div>
                  <button class="btn-detail" @click="showTicketCode(order)">取票码</button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 我的收藏 -->
        <section v-if="activeTab === 'favorites'" class="content-section">
          <h2 class="section-head">我的收藏</h2>
          <div v-if="favoriteMovies.length === 0" class="empty-state">
            <img src="https://p0.meituan.net/movie/60fd63e6e185f577f8841804f32650051234.png" alt="" />
            <p>您还没有收藏任何电影</p>
          </div>
          <div v-else class="favorite-grid">
            <div 
              v-for="movie in favoriteMovies" 
              :key="movie.id" 
              class="fav-card"
              @click="goDetail(movie.id)"
            >
              <div class="fav-poster-wrapper">
                <img :src="movie.posterUrl" class="fav-poster" />
                <div class="remove-btn" @click.stop="handleRemoveFavorite(movie.id)">
                  <el-icon><Close /></el-icon>
                </div>
              </div>
              <div class="fav-info">
                <h4 class="fav-title">{{ movie.title }}</h4>
                <p class="fav-meta">{{ movie.genre }} · {{ movie.rating }}分</p>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { User, Ticket, Star, Setting, Close } from '@element-plus/icons-vue';
import { storeToRefs } from 'pinia';
import { useUserStore } from "../../store/userInfo";
import { userOrders } from '../../mock/data';
import { ElMessage, ElMessageBox } from 'element-plus';
import { updateUserInfoApi } from '../../api/user';
import { getMoviesByIdsApi, toggleFavoriteApi } from '../../api/movie';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const { state: user } = storeToRefs(userStore);

const defaultAvatar = new URL('../../assets/avatar/avatar1.png', import.meta.url).href;
const activeTab = ref('profile');
const saving = ref(false);
const favoriteMovies = ref<any[]>([]);

const menuItems = [
  { id: 'profile', label: '基本设置', icon: User },
  { id: 'records', label: '我的订单', icon: Ticket },
  { id: 'favorites', label: '我的收藏', icon: Star },
  { id: 'security', label: '账号安全', icon: Setting },
];

const profileData = reactive({
  nickname: user.value.nickname,
  phone: user.value.phone,
  bio: user.value.bio
});

const orderList = ref(userOrders);

// 加载收藏的电影详情
const loadFavoriteMovies = async () => {
  if (!user.value.selected || user.value.selected.length === 0) {
    favoriteMovies.value = [];
    return;
  }
  try {
    const res: any = await getMoviesByIdsApi(user.value.selected);
    favoriteMovies.value = res || [];
  } catch (error) {
    console.error('Failed to load favorite movies:', error);
  }
};

const goDetail = (id: number) => {
  router.push(`/movieDetail/${id}`);
};

const handleRemoveFavorite = async (movieId: number) => {
  try {
    const res: any = await toggleFavoriteApi(Number(user.value.id), movieId);
    // 同步到全局 Store
    userStore.setUserInfo({ selected: res || [] });
    // 本地列表即时过滤
    favoriteMovies.value = favoriteMovies.value.filter(m => m.id !== movieId);
    ElMessage.success('已移出收藏');
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const showTicketCode = (order: any) => {
  ElMessageBox.alert(
    `<div style="text-align:center;">
      <p style="font-size:14px;color:#94a3b8;margin-bottom:10px;">出示取票码给自助取票机</p>
      <h2 style="font-size:32px;letter-spacing:4px;color:#15b8a6;margin:20px 0;">8848 1234</h2>
      <img src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=TICKET-${order.id}" style="width:150px;height:150px;" />
    </div>`,
    '电影票电子码',
    { dangerouslyUseHTMLString: true, confirmButtonText: '我知道了' }
  );
};

const saveProfile = async () => {
  if (!user.value.id) {
    ElMessage.error('用户未登录');
    return;
  }
  
  saving.value = true;
  try {
    const res: any = await updateUserInfoApi({
      id: user.value.id,
      nickname: profileData.nickname,
      phone: profileData.phone,
      bio: profileData.bio
    });
    
    if (res && typeof res === 'object' && res.id) {
      userStore.setUserInfo({
        nickname: res.nickname,
        phone: res.phone,
        bio: res.bio
      });
      ElMessage.success('个人信息保存成功');
    } else {
      ElMessage.error(res || '保存失败');
    }
  } catch (error) {
    console.error('Update profile failed:', error);
  } finally {
    saving.value = false;
  }
};

onMounted(() => {
  if (route.query.tab) {
    activeTab.value = route.query.tab as string;
  }
  if (activeTab.value === 'favorites') {
    loadFavoriteMovies();
  }
});

watch(activeTab, (newVal) => {
  if (newVal === 'favorites') {
    loadFavoriteMovies();
  }
});

watch(() => user.value.selected, () => {
  if (activeTab.value === 'favorites') {
    loadFavoriteMovies();
  }
}, { deep: true });

watch(user, (newVal) => {
  profileData.nickname = newVal.nickname;
  profileData.phone = newVal.phone;
  profileData.bio = newVal.bio;
}, { deep: true });
</script>

<style lang="scss" scoped>
.profile-page {
  background: #f7f8fb;
  min-height: calc(100vh - 68px);
  padding: 40px 0;

  .content-wrapper {
    max-width: 1500px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 2fr 8fr;
    gap: 30px;
    padding: 0 16px;
  }

  .profile-sidebar {
    background: #fff;
    border-radius: 16px;
    overflow: hidden;
    height: fit-content;
    box-shadow: 0 4px 20px rgba(0,0,0,0.03);

    .user-brief {
      padding: 40px 20px;
      text-align: center;
      background: linear-gradient(to bottom, #f0f9f8, #ffffff);
      border-bottom: 1px solid #f1f3f6;

      .avatar-wrapper {
        width: 100px; height: 100px; margin: 0 auto 16px;
        position: relative; border-radius: 50%; overflow: hidden;
        border: 4px solid #fff; box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        cursor: pointer;
        img { width: 100%; height: 100%; object-fit: cover; }
        .upload-mask {
          position: absolute; inset: 0; background: rgba(0,0,0,0.4);
          color: #fff; font-size: 12px; display: flex; align-items: center;
          justify-content: center; opacity: 0; transition: opacity 0.3s;
        }
        &:hover .upload-mask { opacity: 1; }
      }
      .user-name { font-size: 18px; font-weight: 700; color: #1f2937; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    }

    .side-menu {
      padding: 10px;
      .menu-item {
        width: 100%; height: 50px; border: none; background: transparent;
        display: flex; align-items: center; gap: 12px; padding: 0 20px;
        border-radius: 10px; cursor: pointer; color: #4b5563; font-weight: 600;
        transition: all 0.2s;
        margin-bottom: 4px;

        .el-icon { font-size: 18px; }
        &:hover { background: #f3f4f6; color: #15b8a6; }
        &.active { background: #15b8a6; color: #fff; box-shadow: 0 4px 12px rgba(21, 184, 166, 0.2); }
      }
    }
  }

  .profile-content {
    background: #fff;
    border-radius: 16px;
    padding: 30px;
    min-height: 600px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.03);

    .section-head {
      font-size: 20px; font-weight: 700; color: #1f2937;
      margin-bottom: 30px; padding-bottom: 16px; border-bottom: 1px solid #f1f3f6;
    }

    .info-form {
      max-width: 800px;
      .form-item {
        margin-bottom: 24px;
        label { display: block; font-size: 14px; color: #6b7280; margin-bottom: 8px; font-weight: 600; }
      }
      .form-actions {
        margin-top: 40px;
        .btn-save {
          background: #15b8a6; color: #fff; border: none; padding: 12px 30px;
          border-radius: 8px; font-weight: 600; cursor: pointer; transition: all 0.3s;
          &:hover { opacity: 0.9; transform: translateY(-1px); }
        }
      }
    }

    .order-list {
      display: grid; gap: 20px;
      .order-card {
        border: 1px solid #f1f3f6; border-radius: 12px; overflow: hidden;
        transition: all 0.3s;
        &:hover { border-color: #15b8a6; box-shadow: 0 8px 24px rgba(21, 184, 166, 0.08); }
        
        .order-header {
          background: #f9fafb; padding: 12px 20px; font-size: 12px; color: #9ca3af;
          display: flex; justify-content: space-between;
        }
        .order-body {
          padding: 20px; display: flex; justify-content: space-between; align-items: center;
          
          .movie-info {
            display: flex; gap: 16px;
            .detail {
              .title { font-size: 16px; font-weight: 700; margin-bottom: 6px; color: #1f2937; }
              .cinema { font-size: 13px; color: #4b5563; margin-bottom: 4px; }
              .hall-time { font-size: 12px; color: #9ca3af; margin-bottom: 8px; }
              .seats { display: flex; gap: 6px;
                .seat-tag { background: #f3f4f6; color: #6b7280; font-size: 11px; padding: 2px 6px; border-radius: 4px; }
              }
            }
          }

          .order-status {
            text-align: right;
            .status-tag { font-size: 13px; font-weight: 700; margin-bottom: 12px;
              &.paid { color: #15b8a6; }
            }
            .btn-detail {
              background: #fff; border: 1px solid #15b8a6; color: #15b8a6;
              padding: 6px 16px; border-radius: 6px; cursor: pointer; font-size: 13px;
              transition: all 0.2s;
              &:hover { background: #15b8a6; color: #fff; }
            }
          }
        }
      }
    }

    .favorite-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
      gap: 24px;
      
      .fav-card {
        cursor: pointer;
        transition: all 0.3s;
        
        .fav-poster-wrapper {
          position: relative;
          width: 100%;
          aspect-ratio: 2/3;
          margin-bottom: 12px;
          border-radius: 12px;
          overflow: hidden;
          box-shadow: 0 4px 12px rgba(0,0,0,0.1);

          .fav-poster {
            width: 100%; height: 100%; object-fit: cover;
            transition: transform 0.5s;
          }

          .remove-btn {
            position: absolute;
            top: 8px;
            right: 8px;
            width: 28px;
            height: 28px;
            background: rgba(0,0,0,0.5);
            backdrop-filter: blur(4px);
            color: #fff;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transform: scale(0.8);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            z-index: 2;

            &:hover {
              background: #ef4444;
              transform: scale(1.1) !important;
            }
          }
        }

        &:hover {
          transform: translateY(-5px);
          .fav-poster { transform: scale(1.05); }
          .remove-btn { opacity: 1; transform: scale(1); }
        }
        
        .fav-title {
          font-size: 15px;
          font-weight: 700;
          color: #1f2937;
          margin-bottom: 4px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        
        .fav-meta {
          font-size: 12px;
          color: #9ca3af;
        }
      }
    }

    .empty-state {
      text-align: center; padding: 60px 0;
      img { width: 120px; opacity: 0.5; margin-bottom: 20px; }
      p { color: #9ca3af; font-size: 14px; }
    }
  }
}
</style>
