<template>
  <div class="order-success-page">
    <div class="content-wrapper">
      <div class="success-card">
        <div class="success-icon">
          <el-icon><CircleCheckFilled /></el-icon>
        </div>
        <h1 class="title">支付成功</h1>
        <p class="subtitle" v-if="order">订单号：{{ order.orderNo }}</p>

        <div class="ticket-info" v-if="order">
          <div class="qr-box">
            <img :src="`https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=TICKET-${order.ticketCode}`" alt="取票二维码" />
            <div class="qr-mask">待取票</div>
          </div>
          <div class="code-box">
            <span class="label">取票码</span>
            <span class="code">{{ order.ticketCode }}</span>
          </div>
        </div>

        <div class="tips">
          <p><el-icon><InfoFilled /></el-icon> 请至影院自助取票机输入取票码或扫描二维码取票</p>
        </div>

        <div class="actions">
          <button class="btn btn-primary" @click="router.push('/profile?tab=records')">查看订单</button>
          <button class="btn btn-secondary" @click="router.push('/home')">返回首页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { CircleCheckFilled, InfoFilled } from '@element-plus/icons-vue';
import { getOrderDetailApi } from '../api/order';
import type { Order } from '../types';

const route = useRoute();
const router = useRouter();

const orderNo = ref(route.query.orderNo as string || '');
const order = ref<Order | null>(null);

const loadOrder = async () => {
  if (!orderNo.value) return;
  try {
    const res = await getOrderDetailApi(orderNo.value);
    order.value = res;
  } catch (error) {
    console.error('Failed to load order detail:', error);
  }
};

onMounted(() => {
  loadOrder();
});
</script>

<style lang="scss" scoped>
.order-success-page {
  min-height: calc(100vh - 68px);
  background: #f7f8fb;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;

  .content-wrapper {
    width: 100%;
    max-width: 600px;
  }

  .success-card {
    background: #fff;
    border-radius: 20px;
    padding: 60px 40px;
    text-align: center;
    box-shadow: 0 10px 40px rgba(0,0,0,0.05);

    .success-icon {
      font-size: 80px;
      color: #15b8a6;
      margin-bottom: 20px;
    }

    .title {
      font-size: 32px;
      font-weight: 800;
      color: #1f2937;
      margin-bottom: 12px;
    }

    .subtitle {
      font-size: 14px;
      color: #94a3b8;
      margin-bottom: 50px;
    }

    .ticket-info {
      background: #f8fafc;
      border-radius: 16px;
      padding: 40px;
      margin-bottom: 40px;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 30px;

      .qr-box {
        width: 200px;
        height: 200px;
        background: #fff;
        padding: 10px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.06);
        position: relative;
        img { width: 100%; height: 100%; }
        .qr-mask {
          position: absolute; bottom: 0; left: 0; right: 0;
          background: rgba(21, 184, 166, 0.9); color: #fff;
          font-size: 12px; font-weight: 700; padding: 4px 0;
          border-radius: 0 0 12px 12px;
        }
      }

      .code-box {
        .label { display: block; font-size: 14px; color: #64748b; margin-bottom: 8px; }
        .code { font-size: 36px; font-weight: 800; color: #15b8a6; letter-spacing: 4px; }
      }
    }

    .tips {
      font-size: 14px;
      color: #64748b;
      margin-bottom: 40px;
      p { display: flex; align-items: center; justify-content: center; gap: 6px; }
    }

    .actions {
      display: flex;
      gap: 20px;
      justify-content: center;

      .btn {
        padding: 14px 40px;
        border-radius: 12px;
        font-size: 16px;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.3s;
        border: none;
      }

      .btn-primary {
        background: #15b8a6;
        color: #fff;
        box-shadow: 0 8px 20px rgba(21, 184, 166, 0.2);
        &:hover { transform: translateY(-2px); opacity: 0.9; }
      }

      .btn-secondary {
        background: #f1f5f9;
        color: #475569;
        &:hover { background: #e2e8f0; }
      }
    }
  }
}
</style>
