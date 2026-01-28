import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePosStore = defineStore('position', () => {
  const position = ref(localStorage.getItem('position') || '上海市')

  const setPosition = (newPosition) => {
    position.value = newPosition
    localStorage.setItem('position', newPosition)
    console.log('位置已更新为:', newPosition)
  }

  return { position, setPosition }
})
