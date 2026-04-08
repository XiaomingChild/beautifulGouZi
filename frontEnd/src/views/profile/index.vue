<template>
  <div class="profile-page">
    <div class="profile-container content-wrapper">
      <!-- 左侧菜单：资深级极简设计 -->
      <aside class="profile-sidebar">
        <div class="user-brief">
          <div class="avatar-group" @click="router.push('/avatar-select')">
            <div class="avatar-ring">
              <img :src="user.avatarUrl || defaultAvatar" alt="用户头像" />
            </div>
            <div class="edit-badge">
              <el-icon><CameraFilled /></el-icon>
            </div>
          </div>
          <div class="user-meta">
            <h3 class="user-name">{{ user.nickname || user.account }}</h3>
          </div>
        </div>
        
        <nav class="side-menu">
          <button 
            v-for="item in menuItems" 
            :key="item.id"
            class="menu-item"
            :class="{ active: activeTab === item.id, 'logout-btn': item.id === 'logout' }"
            @click="handleMenuClick(item.id)"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </button>
        </nav>
      </aside>

      <!-- 右侧内容：大气简约风格 -->
      <main class="profile-content">
        <!-- 基本设置：卡片式精致布局 -->
        <section v-if="activeTab === 'profile'" class="settings-section">
          <header class="section-header">
            <div class="header-title">
              <h2>个人资料</h2>
              <p>管理您的账号基础信息与社交展示</p>
            </div>
          </header>

          <div class="settings-form">
            <div class="form-row">
              <div class="form-group">
                <label>登录账号</label>
                <div class="static-value">{{ user.account }}</div>
                <p class="field-hint">账号为唯一识别码，不可修改</p>
              </div>
            </div>

            <div class="form-grid">
              <div class="form-group">
                <label>个人昵称</label>
                <el-input 
                  v-model="profileData.nickname" 
                  placeholder="展示在影评与购票页的名称"
                  class="custom-input"
                />
              </div>
              <div class="form-group">
                <label>联系电话</label>
                <el-input 
                  v-model="profileData.phone" 
                  placeholder="用于接收购票成功短信"
                  class="custom-input"
                />
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>个性签名</label>
                <el-input 
                  v-model="profileData.bio" 
                  type="textarea" 
                  :rows="4" 
                  placeholder="用一句话介绍你自己..."
                  class="custom-textarea"
                />
              </div>
            </div>

            <div class="form-footer">
              <button class="btn-submit" :class="{ loading: saving }" @click="saveProfile">
                <span v-if="!saving">保存所有修改</span>
                <el-icon v-else class="is-loading"><Loading /></el-icon>
              </button>
            </div>
          </div>
        </section>

        <!-- 我的订单：列表流 -->
        <section v-if="activeTab === 'records'" class="orders-section">
          <header class="section-header">
            <div class="header-title">
              <h2>我的订单</h2>
              <p>查看历史购票记录与取票凭证</p>
            </div>
          </header>
          
          <div v-if="orderList.length === 0" class="empty-state">
            <el-empty description="暂无购票记录" />
          </div>
          <div v-else class="order-flow">
            <div v-for="order in orderList" :key="order.id" class="order-item-card">
              <div class="order-main">
                <div class="movie-poster" v-if="order.schedule?.movie">
                  <img :src="order.schedule.movie.posterUrl" />
                </div>
                <div class="order-details">
                  <div class="top-row">
                    <h4 class="movie-name">《{{ order.schedule?.movie?.title }}》</h4>
                    <span class="status-chip" :class="order.status === 1 ? 'paid' : 'pending'">
                      {{ order.status === 1 ? '已支付' : '待支付' }}
                    </span>
                  </div>
                  <p class="cinema-info">{{ order.schedule?.cinema?.name }} · {{ order.schedule?.hall?.name }}</p>
                  <p class="time-info">{{ order.schedule?.showDate }} {{ order.schedule?.startTime?.substring(0,5) }}</p>
                  <div class="seat-info">
                    <span class="seat-tag" v-for="s in order.seatInfo.split(',')" :key="s">{{ s }}</span>
                  </div>
                </div>
              </div>
              <div class="order-actions">
                <div class="price-info">
                  <span class="currency">￥</span>
                  <span class="amount">{{ order.totalPrice.toFixed(2) }}</span>
                </div>
                <button v-if="order.status === 1" class="btn-ticket" @click="showTicketCode(order)">
                  <el-icon><Ticket /></el-icon> 取票码
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- 我的想看：网格流 -->
        <section v-if="activeTab === 'favorites'" class="fav-section">
          <header class="section-header">
            <div class="header-title">
              <h2>我的想看</h2>
              <p>您感兴趣并标记为收藏的精彩影片</p>
            </div>
          </header>
          <div v-if="favoriteMovies.length === 0" class="empty-state">
            <el-empty description="暂时没有收藏的电影" />
          </div>
          <div class="fav-grid">
            <div 
              v-for="movie in favoriteMovies" 
              :key="movie.id" 
              class="movie-fav-card"
              @click="goDetail(movie.id)"
            >
              <div class="poster-wrap">
                <img :src="movie.posterUrl" />
                <div class="rating-tag">{{ movie.rating }}</div>
                <div class="remove-overlay" @click.stop="handleRemoveFavorite(movie.id)">
                  <el-icon><Delete /></el-icon>
                </div>
              </div>
              <h4 class="movie-title">{{ movie.title }}</h4>
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
import { 
  User, Ticket, Star, SwitchButton, 
  CameraFilled, Loading, Delete 
} from '@element-plus/icons-vue';
import { storeToRefs } from 'pinia';
import { useUserStore } from "../../store/userInfo";
import { ElMessage, ElMessageBox } from 'element-plus';
import { updateUserInfoApi } from '../../api/user';
import { getMoviesByIdsApi, toggleFavoriteApi } from '../../api/movie';
import { getUserOrdersApi } from '../../api/order';
import type { Movie, Order } from '../../types';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const { state: user } = storeToRefs(userStore);

const defaultAvatar = new URL('../../assets/avatar/avatar1.png', import.meta.url).href;
const activeTab = ref('profile');
const saving = ref(false);
const favoriteMovies = ref<Movie[]>([]);
const orderList = ref<Order[]>([]);

const menuItems = [
  { id: 'profile', label: '基本设置', icon: User },
  { id: 'records', label: '我的订单', icon: Ticket },
  { id: 'favorites', label: '我的想看', icon: Star },
  { id: 'logout', label: '退出登录', icon: SwitchButton },
];

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出当前账号吗？', '安全提示', {
    confirmButtonText: '确定退出',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.resetUser();
    ElMessage.success('已安全退出');
    router.push('/login');
  });
};

const handleMenuClick = (id: string) => {
  if (id === 'logout') handleLogout();
  else activeTab.value = id;
};

const profileData = reactive({
  nickname: user.value.nickname,
  phone: user.value.phone,
  bio: user.value.bio
});

const loadOrders = async () => {
  if (!user.value.id) return;
  try {
    const res = await getUserOrdersApi(Number(user.value.id));
    orderList.value = res || [];
  } catch (error) {
    console.error('Order load failed:', error);
  }
};

const loadFavoriteMovies = async () => {
  if (!user.value.favoriteMovieIds?.length) {
    favoriteMovies.value = [];
    return;
  }
  try {
    const res = await getMoviesByIdsApi(user.value.favoriteMovieIds);
    favoriteMovies.value = res || [];
  } catch (error) {
    console.error('Favorite load failed:', error);
  }
};

const handleRemoveFavorite = async (movieId: number) => {
  try {
    const res = await toggleFavoriteApi(Number(user.value.id), movieId);
    userStore.setUserInfo({ favoriteMovieIds: res || [] });
    favoriteMovies.value = favoriteMovies.value.filter(m => m.id !== movieId);
    ElMessage.success('已从想看列表移除');
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const showTicketCode = (order: Order) => {
  ElMessageBox.alert(
    `<div class="ticket-dialog-content">
      <div class="ticket-qr-wrap">
        <img src="https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=TICKET-${order.ticketCode}" />
      </div>
      <div class="ticket-info-wrap">
        <p class="ticket-label">唯一取票码</p>
        <h2 class="ticket-code-text">${order.ticketCode}</h2>
        <div class="ticket-warning">请在影院自助机扫描或输入此码</div>
      </div>
    </div>`,
    '电影票电子码',
    { dangerouslyUseHTMLString: true, confirmButtonText: '完成' }
  );
};

const saveProfile = async () => {
  if (!user.value.id) return;
  saving.value = true;
  try {
    const res = await updateUserInfoApi({
      id: user.value.id,
      nickname: profileData.nickname,
      phone: profileData.phone,
      bio: profileData.bio
    });
    userStore.setUserInfo(res);
    ElMessage.success('资料已同步更新');
  } catch (error) {
    ElMessage.error('保存失败');
  } finally {
    saving.value = false;
  }
};

const goDetail = (id: number) => router.push(`/movieDetail/${id}`);

onMounted(() => {
  if (route.query.tab) activeTab.value = route.query.tab as string;
  if (activeTab.value === 'favorites') loadFavoriteMovies();
  if (activeTab.value === 'records') loadOrders();
});

watch(activeTab, (v) => {
  if (v === 'favorites') loadFavoriteMovies();
  if (v === 'records') loadOrders();
});

watch(() => user.value.favoriteMovieIds, () => {
  if (activeTab.value === 'favorites') loadFavoriteMovies();
}, { deep: true });

watch(user, (v) => {
  profileData.nickname = v.nickname;
  profileData.phone = v.phone;
  profileData.bio = v.bio;
}, { deep: true });
</script>

<style lang="scss" scoped>
.profile-page {
  background: #fcfcfd;
  min-height: calc(100vh - 68px);
  padding: 60px 0;

  .content-wrapper {
    max-width: 1280px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 320px 1fr;
    gap: 40px;
    padding: 0 24px;
  }

  /* 侧边栏样式 */
  .profile-sidebar {
    background: #fff;
    border-radius: 24px;
    padding: 40px 20px;
    height: fit-content;
    box-shadow: 0 10px 40px rgba(0,0,0,0.02);
    border: 1px solid #f1f5f9;

    .user-brief {
      text-align: center;
      margin-bottom: 40px;

      .avatar-group {
        position: relative;
        width: 120px;
        height: 120px;
        margin: 0 auto 20px;
        cursor: pointer;

        .avatar-ring {
          width: 100%; height: 100%; border-radius: 50%; padding: 4px;
          border: 2px solid #f1f5f9; overflow: hidden;
          img { width: 100%; height: 100%; border-radius: 50%; object-fit: cover; }
        }

        .edit-badge {
          position: absolute; bottom: 4px; right: 4px;
          background: #15b8a6; color: #fff; width: 32px; height: 32px;
          border-radius: 50%; display: flex; align-items: center; justify-content: center;
          box-shadow: 0 4px 10px rgba(21, 184, 166, 0.3);
          border: 3px solid #fff;
        }

        &:hover .avatar-ring { border-color: #15b8a6; }
      }

      .user-name { font-size: 20px; font-weight: 800; color: #1e293b; margin-bottom: 4px; }
      .user-id { font-size: 13px; color: #94a3b8; }
    }

    .side-menu {
      .menu-item {
        width: 100%; height: 52px; border: none; background: transparent;
        display: flex; align-items: center; gap: 14px; padding: 0 24px;
        border-radius: 14px; cursor: pointer; color: #64748b; font-weight: 600;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        margin-bottom: 8px;

        .el-icon { font-size: 20px; }
        &:hover { background: #f8fafc; color: #15b8a6; }
        &.active { background: #15b8a6; color: #fff; box-shadow: 0 10px 20px rgba(21, 184, 166, 0.2); }
        
        &.logout-btn {
          margin-top: 40px; color: #f43f5e;
          &:hover { background: #fff1f2; }
        }
      }
    }
  }

  /* 主体内容样式 */
  .profile-content {
    background: #fff;
    border-radius: 24px;
    padding: 40px;
    box-shadow: 0 10px 40px rgba(0,0,0,0.02);
    border: 1px solid #f1f5f9;

    .section-header {
      margin-bottom: 40px;
      .header-title {
        h2 { font-size: 24px; font-weight: 800; color: #1e293b; margin-bottom: 8px; }
        p { font-size: 14px; color: #94a3b8; }
      }
    }

    /* 基本设置表单 */
    .settings-form {
      .form-grid {
        display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 30px;
      }
      .form-row { margin-bottom: 30px; }
      
      .form-group {
        label { display: block; font-size: 14px; font-weight: 700; color: #475569; margin-bottom: 12px; }
        .static-value { font-size: 16px; color: #1e293b; font-weight: 600; padding: 12px 0; }
        .field-hint { font-size: 12px; color: #cbd5e1; margin-top: 8px; }
      }

      .btn-submit {
        background: #1e293b; color: #fff; border: none; padding: 16px 40px;
        border-radius: 12px; font-weight: 700; cursor: pointer; transition: all 0.3s;
        margin-top: 20px;
        &:hover { background: #0f172a; transform: translateY(-2px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        &.loading { opacity: 0.7; cursor: wait; }
      }
    }

    /* 订单样式 */
    .order-item-card {
      border: 1px solid #f1f5f9; border-radius: 20px; margin-bottom: 24px;
      display: flex; justify-content: space-between; padding: 24px;
      transition: all 0.3s;
      &:hover { border-color: #15b8a6; background: #fafdfd; }

      .order-main {
        display: flex; gap: 20px;
        .movie-poster { width: 80px; height: 110px; border-radius: 10px; overflow: hidden;
          img { width: 100%; height: 100%; object-fit: cover; }
        }
        .order-details {
          .top-row { display: flex; align-items: center; gap: 12px; margin-bottom: 10px;
            .movie-name { font-size: 18px; font-weight: 800; color: #1e293b; }
            .status-chip { font-size: 11px; padding: 2px 10px; border-radius: 20px; font-weight: 700;
              &.paid { background: #ccfbf1; color: #0f766e; }
              &.pending { background: #fef3c7; color: #b45309; }
            }
          }
          .cinema-info { font-size: 14px; color: #64748b; margin-bottom: 4px; }
          .time-info { font-size: 13px; color: #94a3b8; margin-bottom: 10px; }
          .seat-info { display: flex; gap: 8px; .seat-tag { background: #f1f5f9; color: #475569; padding: 2px 8px; border-radius: 6px; font-size: 11px; } }
        }
      }

      .order-actions {
        display: flex; flex-direction: column; align-items: flex-end; justify-content: space-between;
        .price-info { .currency { font-size: 12px; color: #f43f5e; font-weight: 700; } .amount { font-size: 24px; color: #f43f5e; font-weight: 800; } }
        .btn-ticket { background: #fff; border: 1.5px solid #15b8a6; color: #15b8a6; padding: 8px 20px; border-radius: 10px; font-size: 13px; font-weight: 700; cursor: pointer; display: flex; align-items: center; gap: 6px; transition: all 0.2s; &:hover { background: #15b8a6; color: #fff; } }
      }
    }

    /* 想看网格 */
    .fav-grid {
      display: grid; grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 30px;
      .movie-fav-card {
        cursor: pointer;
        .poster-wrap {
          position: relative; border-radius: 16px; overflow: hidden; aspect-ratio: 2/3; box-shadow: 0 10px 25px rgba(0,0,0,0.08);
          img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
          .rating-tag { position: absolute; top: 10px; left: 10px; background: rgba(0,0,0,0.6); color: #fbbf24; padding: 2px 8px; border-radius: 6px; font-size: 12px; font-weight: 800; backdrop-filter: blur(4px); }
          .remove-overlay { position: absolute; inset: 0; background: rgba(244, 63, 94, 0.8); display: flex; align-items: center; justify-content: center; color: #fff; font-size: 32px; opacity: 0; transition: opacity 0.3s; backdrop-filter: blur(4px); }
          &:hover .remove-overlay { opacity: 1; }
          &:hover img { transform: scale(1.1); }
        }
        .movie-title { margin-top: 14px; font-size: 15px; font-weight: 700; color: #1e293b; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
      }
    }
  }
}
</style>

<style lang="scss">
/* 全局覆盖弹窗样式 */
.ticket-dialog-content {
  text-align: center; padding: 20px 0;
  .ticket-qr-wrap { background: #f8fafc; padding: 20px; border-radius: 20px; display: inline-block; margin-bottom: 24px; img { width: 180px; height: 180px; } }
  .ticket-info-wrap {
    .ticket-label { font-size: 13px; color: #94a3b8; margin-bottom: 8px; }
    .ticket-code-text { font-size: 36px; color: #15b8a6; letter-spacing: 6px; font-weight: 900; margin-bottom: 12px; }
    .ticket-warning { font-size: 12px; color: #f43f5e; background: #fff1f2; padding: 4px 12px; border-radius: 20px; display: inline-block; }
  }
}

/* 覆盖Element Plus 输入框 */
.custom-input .el-input__wrapper, .custom-textarea .el-textarea__inner {
  background-color: #f8fafc !important;
  box-shadow: none !important;
  border: 1px solid #e2e8f0 !important;
  border-radius: 12px !important;
  padding: 12px 16px !important;
  &:hover { border-color: #cbd5e1 !important; }
  &.is-focus { border-color: #15b8a6 !important; background-color: #fff !important; }
}
</style>
