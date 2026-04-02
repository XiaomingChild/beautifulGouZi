import { defineStore } from 'pinia';
import { reactive } from 'vue';
import type { User } from '../types';

export const useUserStore = defineStore('user', () => {
  // 默认使用本地第一个头像
  const defaultAvatar = new URL('../assets/avatar/avatar1.png', import.meta.url).href;

  const state = reactive<User>({
    id: 0,
    account: '',
    nickname: '',
    token: '',
    avatar: defaultAvatar,
    phone: '',
    bio: '',
    selected: [] // 收藏的电影ID列表
  });

  const hydrate = () => {
    const saved = localStorage.getItem('userInfo');
    if (!saved) return;
    try {
      const data = JSON.parse(saved);
      if (!data.avatar) data.avatar = defaultAvatar;
      if (!data.selected) data.selected = [];
      Object.assign(state, data);
    } catch (e) {
      console.warn('userInfo hydrate failed', e);
    }
  };

  const setUserInfo = (newInfo: Partial<User>) => {
    Object.keys(newInfo).forEach(key => {
      const k = key as keyof User;
      if (newInfo[k] !== undefined && newInfo[k] !== null) {
        (state as any)[k] = newInfo[k];
      }
    });
    localStorage.setItem('userInfo', JSON.stringify(state));
  };

  const resetUser = () => {
    state.id = 0;
    state.account = '';
    state.nickname = '';
    state.token = '';
    state.avatar = defaultAvatar;
    state.phone = '';
    state.bio = '';
    state.selected = [];
    localStorage.removeItem('userInfo');
  };

  const isAuthed = () => !!state.token;

  hydrate();

  return { state, setUserInfo, resetUser, isAuthed };
});
