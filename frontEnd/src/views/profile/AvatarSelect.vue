<template>
  <div class="avatar-select-container">
    <div class="header">
      <div class="back-btn" @click="router.back()">
        <el-icon><ArrowLeftBold /></el-icon>
        <span>返回个人中心</span>
      </div>
      <h2>选择你的个性头像</h2>
    </div>
    
    <div class="avatar-grid">
      <div 
        v-for="i in 15" 
        :key="i" 
        class="avatar-item"
        :class="{ active: currentAvatarUrl === getAvatarPath(i) }"
        @click="selectAvatar(i)"
      >
        <div class="img-box">
          <img :src="getAvatarPath(i)" alt="头像预选" />
        </div>
        <div class="check-mask" v-if="currentAvatarUrl === getAvatarPath(i)">
          <el-icon><Check /></el-icon>
        </div>
      </div>
    </div>

    <div class="footer">
      <button class="btn-confirm" @click="saveAvatar">确认更换</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../../store/userInfo';
import { ElMessage } from 'element-plus';
import { Check, ArrowLeftBold } from '@element-plus/icons-vue';
import { updateUserInfoApi } from '../../api/user';

const router = useRouter();
const userStore = useUserStore();

// 核心：使用更稳健的 Vite 静态资源获取方式
const getAvatarPath = (i: number) => {
  return new URL(`../../assets/avatar/avatar${i}.png`, import.meta.url).href;
};

const currentAvatarUrl = ref('');

onMounted(() => {
  // 初始化当前选中的头像，确保使用正确的属性名 avatarUrl
  currentAvatarUrl.value = userStore.state.avatarUrl || getAvatarPath(1);
});

const selectAvatar = (i: number) => {
  currentAvatarUrl.value = getAvatarPath(i);
};

const saveAvatar = async () => {
  if (!currentAvatarUrl.value) return;
  
  try {
    if (userStore.state.id) {
      // 1. 同步到后端数据库
      await updateUserInfoApi({ 
        id: userStore.state.id, 
        avatarUrl: currentAvatarUrl.value 
      });
      // 2. 同步到全局 Pinia Store（触发导航栏等组件的实时更新）
      userStore.setUserInfo({ avatarUrl: currentAvatarUrl.value });
      
      ElMessage.success('头像更新成功');
      router.back();
    }
  } catch (error) {
    ElMessage.error('网络繁忙，保存失败');
  }
};
</script>

<style scoped lang="scss">
.avatar-select-container {
  max-width: 1000px;
  margin: 60px auto;
  padding: 40px;
  background: #fff;
  border-radius: 32px;
  box-shadow: 0 30px 80px rgba(0,0,0,0.05);

  .header {
    margin-bottom: 50px;
    .back-btn {
      display: inline-flex; align-items: center; gap: 8px;
      color: #94a3b8; cursor: pointer; transition: all 0.3s;
      font-weight: 600; margin-bottom: 16px;
      &:hover { color: #15b8a6; transform: translateX(-4px); }
    }
    h2 { font-size: 28px; font-weight: 800; color: #1e293b; margin: 0; }
  }

  .avatar-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 30px;
    margin-bottom: 60px;
  }

  .avatar-item {
    position: relative;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);

    .img-box {
      aspect-ratio: 1; border-radius: 24px; overflow: hidden;
      border: 4px solid #f8fafc; background: #f8fafc;
      transition: all 0.3s;
      img { width: 100%; height: 100%; object-fit: cover; }
    }

    &:hover { transform: translateY(-8px); }

    &.active {
      .img-box { border-color: #15b8a6; box-shadow: 0 15px 30px rgba(21, 184, 166, 0.2); }
    }

    .check-mask {
      position: absolute; inset: 0; border-radius: 24px;
      background: rgba(21, 184, 166, 0.5);
      display: flex; align-items: center; justify-content: center;
      color: #fff; font-size: 40px;
      backdrop-filter: blur(2px);
    }
  }

  .footer {
    text-align: center;
    .btn-confirm {
      background: #1e293b; color: #fff; border: none;
      padding: 18px 120px; border-radius: 16px;
      font-size: 16px; font-weight: 800; cursor: pointer;
      transition: all 0.3s;
      box-shadow: 0 10px 30px rgba(0,0,0,0.1);
      &:hover { background: #0f172a; transform: scale(1.02); }
    }
  }
}
</style>
