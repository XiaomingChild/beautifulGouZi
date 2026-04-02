<template>
  <div class="seat-selection">
    <div class="selection-inner">
      <!-- 左侧选座区 -->
      <div class="seat-main">
        <div class="screen-area">
          <div class="screen-box">
            <span class="screen-text">屏幕中央</span>
          </div>
          <div class="screen-light"></div>
        </div>

        <div class="seat-grid">
          <div v-for="row in 8" :key="row" class="seat-row">
            <span class="row-index">{{ row }}</span>
            <div class="row-seats">
              <div
                v-for="col in 10"
                :key="col"
                class="seat-item"
                :class="getSeatStatus(row, col)"
                @click="toggleSeat(row, col)"
              >
                <el-tooltip
                  :content="`${row}排${col}座`"
                  placement="top"
                  :disabled="isSold(row, col)"
                >
                  <div class="seat-inner"></div>
                </el-tooltip>
              </div>
            </div>
          </div>
        </div>

        <!-- 图例说明 -->
        <div class="legend">
          <div class="legend-item"><div class="seat-inner"></div><span>可选</span></div>
          <div class="legend-item"><div class="seat-inner selected"></div><span>已选</span></div>
          <div class="legend-item"><div class="seat-inner sold"></div><span>已售</span></div>
        </div>
      </div>

      <!-- 右侧订单确认 -->
      <aside class="order-side">
        <div class="movie-card">
          <img :src="movie.imgUrl" class="poster" alt="" />
          <div class="movie-info">
            <h3 class="title">{{ movie.title }}</h3>
            <p class="meta">{{ movie.tags.join('/') }}</p>
            <p class="meta">{{ movie.duration }}分钟</p>
          </div>
        </div>

        <div class="showtime-info">
          <div class="info-row">
            <span class="label">影院：</span>
            <span class="value">{{ schedule.cinemaName }}</span>
          </div>
          <div class="info-row">
            <span class="label">影厅：</span>
            <span class="value">{{ schedule.hall }}</span>
          </div>
          <div class="info-row">
            <span class="label">场次：</span>
            <span class="value accent">{{ schedule.startTime }} ({{ schedule.type }})</span>
          </div>
        </div>

        <div class="selection-summary">
          <p class="section-title">座位：</p>
          <div v-if="selectedSeats.length === 0" class="empty-seats">尚未选座</div>
          <div v-else class="selected-chips">
            <span v-for="s in selectedSeats" :key="s.id" class="seat-chip">
              {{ s.row }}排{{ s.col }}座
            </span>
          </div>
        </div>

        <div class="price-box">
          <div class="total">
            <span class="label">总价：</span>
            <span class="currency">￥</span>
            <span class="amount">{{ totalPrice.toFixed(2) }}</span>
          </div>
          <button class="btn-confirm" :disabled="selectedSeats.length === 0" @click="handleConfirm">
            确认下单
          </button>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { movies, schedules } from '../mock/data';

const route = useRoute();
const router = useRouter();

// 模拟数据初始化
const movie = ref(movies[0]);
const schedule = ref({
  ...schedules[0],
  cinemaName: '万达影城（CBD店）'
});

// 已售座位的简单模拟（随机几个座位）
const soldSeats = ref(['2-4', '2-5', '5-7', '5-8', '8-1', '8-2']);

interface SelectedSeat {
  id: string;
  row: number;
  col: number;
}
const selectedSeats = ref<SelectedSeat[]>([]);

const getSeatStatus = (row: number, col: number) => {
  const id = `${row}-${col}`;
  if (soldSeats.value.includes(id)) return 'sold';
  if (selectedSeats.value.some(s => s.id === id)) return 'selected';
  return 'available';
};

const isSold = (row: number, col: number) => soldSeats.value.includes(`${row}-${col}`);

const toggleSeat = (row: number, col: number) => {
  const id = `${row}-${col}`;
  if (soldSeats.value.includes(id)) return;

  const index = selectedSeats.value.findIndex(s => s.id === id);
  if (index > -1) {
    selectedSeats.value.splice(index, 1);
  } else {
    if (selectedSeats.value.length >= 5) {
      ElMessage.warning('每个订单最多购买5张票');
      return;
    }
    selectedSeats.value.push({ id, row, col });
  }
};

const totalPrice = computed(() => selectedSeats.value.length * schedule.value.price);

const handleConfirm = () => {
  ElMessageBox.confirm(
    `确认购买这 ${selectedSeats.value.length} 张票吗？共计 ￥${totalPrice.value.toFixed(2)}`,
    '确认购票',
    { confirmButtonText: '立即支付', cancelButtonText: '再选选' }
  ).then(() => {
    ElMessage.success('支付成功！正在为您生成取票码...');
    setTimeout(() => {
      router.push('/home'); // 实际应跳转到订单页
    }, 1500);
  });
};

onMounted(() => {
  // 逻辑：根据 query 参数加载场次信息...
});
</script>

<style lang="scss" scoped>
.seat-selection {
  background: #f1f3f6;
  min-height: calc(100vh - 68px);
  padding: 40px 20px;

  .selection-inner {
    max-width: 1200px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 1fr 340px;
    gap: 30px;
  }

  .seat-main {
    background: #fff;
    border-radius: 12px;
    padding: 60px 40px;
    display: flex;
    flex-direction: column;
    align-items: center;

    .screen-area {
      width: 80%;
      margin-bottom: 60px;
      position: relative;
      .screen-box {
        height: 40px; border-top: 4px solid #15b8a6; border-radius: 50% / 100% 100% 0 0;
        text-align: center; line-height: 40px; background: rgba(21, 184, 166, 0.05);
        .screen-text { font-size: 14px; color: #94a3b8; }
      }
      .screen-light {
        height: 60px; background: linear-gradient(to bottom, rgba(21, 184, 166, 0.15), transparent);
      }
    }

    .seat-grid {
      display: flex; flex-direction: column; gap: 15px;
      .seat-row {
        display: flex; align-items: center; gap: 20px;
        .row-index { width: 20px; font-size: 12px; color: #94a3b8; text-align: center; }
        .row-seats { display: flex; gap: 10px; }
      }
    }

    .seat-item {
      cursor: pointer;
      .seat-inner {
        width: 26px; height: 24px; background: #e2e8f0; border-radius: 4px; border-bottom: 3px solid #cbd5e1;
        transition: all 0.2s;
        &.selected { background: #15b8a6; border-bottom-color: #0d9488; transform: scale(1.1); }
        &.sold { background: #94a3b8; border-bottom-color: #64748b; cursor: not-allowed; }
      }
      &:not(.sold):hover .seat-inner { transform: scale(1.1); border-color: #15b8a6; }
    }

    .legend {
      margin-top: 60px; display: flex; gap: 40px;
      .legend-item {
        display: flex; align-items: center; gap: 8px; font-size: 14px; color: #64748b;
        .seat-inner { width: 18px; height: 16px; border-bottom-width: 2px; }
      }
    }
  }

  .order-side {
    background: #fff; border-radius: 12px; padding: 24px; height: fit-content;
    display: flex; flex-direction: column; gap: 24px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);

    .movie-card {
      display: flex; gap: 16px; padding-bottom: 24px; border-bottom: 1px dashed #e2e8f0;
      .poster { width: 80px; height: 110px; object-fit: cover; border-radius: 6px; }
      .movie-info {
        .title { font-size: 18px; font-weight: 700; margin-bottom: 10px; }
        .meta { font-size: 13px; color: #94a3b8; margin-bottom: 4px; }
      }
    }

    .showtime-info {
      .info-row {
        margin-bottom: 8px; font-size: 14px; display: flex;
        .label { color: #94a3b8; width: 50px; flex-shrink: 0; }
        .value { color: #1f2937; font-weight: 600; }
        .accent { color: #f97316; }
      }
    }

    .selection-summary {
      .section-title { font-size: 14px; color: #94a3b8; margin-bottom: 12px; }
      .empty-seats { font-size: 14px; color: #cbd5e1; font-style: italic; }
      .selected-chips {
        display: flex; flex-wrap: wrap; gap: 8px;
        .seat-chip {
          background: #e9f7f9; color: #15b8a6; border: 1px solid #15b8a6;
          padding: 4px 10px; border-radius: 6px; font-size: 13px; font-weight: 600;
        }
      }
    }

    .price-box {
      margin-top: 20px; padding-top: 24px; border-top: 1px solid #f1f3f6;
      .total {
        margin-bottom: 20px; display: flex; align-items: baseline;
        .label { font-size: 14px; color: #1f2937; }
        .currency { color: #f97316; font-size: 14px; font-weight: bold; }
        .amount { color: #f97316; font-size: 28px; font-weight: 800; }
      }
      .btn-confirm {
        width: 100%; height: 50px; background: #15b8a6; color: #fff; border: none;
        border-radius: 10px; font-size: 16px; font-weight: 700; cursor: pointer;
        box-shadow: 0 10px 20px rgba(21, 184, 166, 0.25);
        transition: all 0.3s;
        &:disabled { background: #cbd5e1; cursor: not-allowed; box-shadow: none; }
        &:not(:disabled):hover { transform: translateY(-2px); opacity: 0.9; }
      }
    }
  }
}
</style>
