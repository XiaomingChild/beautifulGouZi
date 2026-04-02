<template>
  <div class="chips">
    <button
      v-for="item in categories"
      :key="item.value"
      class="chip"
      :class="{ active: item.value === modelValue }"
      type="button"
      @click="$emit('update:modelValue', item.value)"
    >
      {{ item.label }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  modelValue: string;
  categories: Array<{ label: string; value: string }>;
}>();

defineEmits(['update:modelValue']);
</script>

<style lang="scss" scoped>
.chips {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.chip {
  padding: 8px 20px;
  border-radius: 20px;
  border: 1px solid #e5e7eb;
  background: #fff;
  color: #4b5563;
  cursor: pointer;
  white-space: nowrap;
  &.active { background: #15b8a6; color: #fff; border-color: #15b8a6; }
}
</style>
