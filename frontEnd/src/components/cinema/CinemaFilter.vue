<template>
  <div class="filter-header">
    <div class="content-wrapper">
      <div class="filter-row">
        <span class="label">行政区：</span>
        <div class="options">
          <span 
            class="option" 
            :class="{ active: activeArea === '全部' }"
            @click="$emit('update:activeArea', '全部')"
          >
            全部
          </span>
          <span 
            v-for="area in areas" 
            :key="area" 
            class="option"
            :class="{ active: activeArea === area }"
            @click="$emit('update:activeArea', area)"
          >
            {{ area }}
          </span>
        </div>
      </div>
      <div class="filter-row">
        <span class="label">特殊厅：</span>
        <div class="options">
          <span 
            v-for="hall in specialHalls" 
            :key="hall" 
            class="option"
            :class="{ active: activeHall === hall }"
            @click="$emit('update:activeHall', hall)"
          >
            {{ hall }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  areas: string[];
  specialHalls: string[];
  activeArea: string;
  activeHall: string;
}>();

defineEmits(['update:activeArea', 'update:activeHall']);
</script>

<style lang="scss" scoped>
.filter-header {
  background: #fff;
  padding: 24px 0;
  border-bottom: 1px solid #f1f3f6;
  margin-bottom: 24px;

  .content-wrapper {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .filter-row {
    display: flex;
    margin-bottom: 12px;
    &:last-child { margin-bottom: 0; }

    .label { width: 80px; font-size: 14px; color: #9ca3af; padding-top: 4px; }
    .options {
      flex: 1; display: flex; flex-wrap: wrap; gap: 8px;
      .option {
        padding: 4px 12px; border-radius: 4px; font-size: 14px; cursor: pointer;
        color: #4b5563;
        &:hover { color: #15b8a6; }
        &.active { background: #e9f7f9; color: #15b8a6; font-weight: 600; }
      }
    }
  }
}
</style>
