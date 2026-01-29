import { defineStore } from 'pinia'
import { reactive } from 'vue'
import defaultAvatar from '../assets/avatar/avatar.png'

export const useUserStore = defineStore('user', () => {
  const state = reactive({
    account: '',
    token: '',
    avatar: defaultAvatar
  })

  const hydrate = () => {
    const saved = localStorage.getItem('userInfo')
    if (!saved) return
    try {
      Object.assign(state, JSON.parse(saved))
    } catch (e) {
      console.warn('userInfo hydrate failed', e)
    }
  }

  const setUserInfo = (newInfo) => {
    Object.assign(state, newInfo)
    localStorage.setItem('userInfo', JSON.stringify({
      account: state.account,
      token: state.token,
      avatar: state.avatar
    }))
    console.log('用户信息已更新为:', state)
  }

  const resetUser = () => {
    state.account = ''
    state.token = ''
    state.avatar = defaultAvatar
    localStorage.removeItem('userInfo')
  }

  const isAuthed = () => !!state.token

  hydrate()

  return { state, setUserInfo, resetUser, isAuthed }
})
