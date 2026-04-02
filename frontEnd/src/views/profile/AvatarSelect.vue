<template>
  <div class="avatar-select-container">
    <div class="header">
      <el-icon class="back-arrow" @click="router.back()"><ArrowLeftBold /></el-icon>
    </div>
    
    <div class="avatar-grid">
      <div 
        v-for="i in 12" 
        :key="i" 
        class="avatar-item"
        :class="{ active: currentAvatar === getAvatarUrl(i) }"
        @click="selectAvatar(i)"
      >
        <img :src="getAvatarUrl(i)" alt="头像预选" />
        <div class="check-mask" v-if="currentAvatar === getAvatarUrl(i)">
          <el-icon><Check /></el-icon>
        </div>
      </div>
    </div>

    <div class="footer">
      <el-button type="primary" size="large" @click="saveAvatar">确认更换</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/userInfo'
import { ElMessage } from 'element-plus'
import { Check, ArrowLeftBold } from '@element-plus/icons-vue'
import { updateUserInfoApi } from '../../api/user'

const router = useRouter()
const userStore = useUserStore()

// 获取当前用户头像
const currentAvatar = ref(userStore.state.avatar)

// 动态生成本地静态头像路径
const getAvatarUrl = (i: number) => {
  return new URL(`../../assets/avatar/avatar${i}.png`, import.meta.url).href
}

const selectAvatar = (i: number) => {
  currentAvatar.value = getAvatarUrl(i)
}

const saveAvatar = async () => {
  try {
    // 1. 同步到本地 store
    userStore.setUserInfo({ avatar: currentAvatar.value })
    
    // 2. 如果用户已登录，同步到后端
    if (userStore.state.id) {
      await updateUserInfoApi({ 
        id: userStore.state.id, 
        avatar: currentAvatar.value 
      })
    }
    
    ElMessage.success('头像修改成功')
    router.back() // 修改成功后返回上一页（个人中心）
  } catch (error) {
    console.error('Save avatar failed:', error)
    ElMessage.error('头像保存失败')
  }
}
</script>

<style scoped lang="scss">
.avatar-select-container {
  max-width: 800px;
  margin: 60px auto;
  padding: 40px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.08);

  .header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 40px;
    
    .back-arrow {
      font-size: 32px;
      color: #64748b;
      cursor: pointer;
      transition: all 0.2s;
      &:hover {
        color: #15b8a6;
        transform: translateX(-4px);
      }
    }
  }

  .avatar-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24px;
    margin-bottom: 50px;
  }

  .avatar-item {
    aspect-ratio: 1;
    border-radius: 16px;
    overflow: hidden;
    cursor: pointer;
    position: relative;
    border: 4px solid transparent;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    background: #f9fafb;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    &:hover {
      transform: translateY(-5px);
      border-color: #f3f4f6;
    }

    &.active {
      border-color: #15b8a6;
      box-shadow: 0 12px 24px rgba(21, 184, 166, 0.25);
    }

    .check-mask {
      position: absolute;
      top: 0; left: 0; right: 0; bottom: 0;
      background: rgba(21, 184, 166, 0.4);
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 36px;
    }
  }

  .footer {
    text-align: center;
    .el-button {
      padding: 14px 80px;
      font-weight: 700;
      border-radius: 12px;
      background: #15b8a6;
      border: none;
      font-size: 16px;
      &:hover {
        background: #129c90;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(21, 184, 166, 0.3);
      }
    }
  }
}
</style>