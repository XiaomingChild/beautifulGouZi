import { defineStore } from 'pinia'
import { reactive } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 默认使用本地第一个头像
  const defaultAvatar = new URL('../assets/avatar/avatar1.png', import.meta.url).href

  const state = reactive({
    id: '',
    account: '',
    nickname: '',
    token: '',
    avatar: defaultAvatar,
    phone: '',
    bio: '',
    selected: [] // 收藏的电影ID列表
  })

  const hydrate = () => {
    const saved = localStorage.getItem('userInfo')
    if (!saved) return
    try {
      const data = JSON.parse(saved)
      if (!data.avatar) data.avatar = defaultAvatar
      if (!data.selected) data.selected = []
      Object.assign(state, data)
    } catch (e) {
      console.warn('userInfo hydrate failed', e)
    }
  }

  const setUserInfo = (newInfo) => {
    Object.keys(newInfo).forEach(key => {
      if (newInfo[key] !== undefined && newInfo[key] !== null) {
        state[key] = newInfo[key]
      }
    })
    localStorage.setItem('userInfo', JSON.stringify(state))
  }

  const resetUser = () => {
    state.id = ''
    state.account = ''
    state.nickname = ''
    state.token = ''
    state.avatar = defaultAvatar
    state.phone = ''
    state.bio = ''
    state.selected = []
    localStorage.removeItem('userInfo')
  }

  const isAuthed = () => !!state.token

  hydrate()

  return { state, setUserInfo, resetUser, isAuthed }
})
