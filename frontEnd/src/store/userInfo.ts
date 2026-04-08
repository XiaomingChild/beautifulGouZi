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
    avatarUrl: defaultAvatar,
    phone: '',
    bio: '',
    createdAt: '',
    favoriteMovieIds: []
  });

  const hydrate = () => {
    const saved = localStorage.getItem('userInfo');
    if (!saved) return;
    try {
      const data = JSON.parse(saved);
      if (!data.avatarUrl && data.avatar) data.avatarUrl = data.avatar; // 兼容旧数据
      if (!data.avatarUrl) data.avatarUrl = defaultAvatar;
      if (!data.favoriteMovieIds && data.selected) data.favoriteMovieIds = data.selected; // 兼容旧数据
      if (!data.favoriteMovieIds) data.favoriteMovieIds = [];
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
    state.avatarUrl = defaultAvatar;
    state.phone = '';
    state.bio = '';
    state.createdAt = '';
    state.favoriteMovieIds = [];
    localStorage.removeItem('userInfo');
    localStorage.removeItem('token');
  };

  const isAuthed = () => !!state.account;

  hydrate();

  return { state, setUserInfo, resetUser, isAuthed };
});
