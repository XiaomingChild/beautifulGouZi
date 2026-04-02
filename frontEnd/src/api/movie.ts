import request from '../utils/request';
import type { Movie, PageResult, Schedule, Cinema } from '../types';

/**
 * 获取正在热映电影
 */
export function getHotMoviesApi(genre: string = 'all', page: number = 0, size: number = 10) {
  return request<PageResult<Movie>>({
    url: '/movies/hot',
    method: 'get',
    params: { genre, page, size }
  });
}

/**
 * 获取即将上映电影
 */
export function getUpcomingMoviesApi(genre: string = 'all', page: number = 0, size: number = 10) {
  return request<PageResult<Movie>>({
    url: '/movies/upcoming',
    method: 'get',
    params: { genre, page, size }
  });
}

/**
 * 根据分类获取电影
 */
export function getMoviesByCategoryApi(tag: string, page: number = 0, size: number = 10) {
  return request<PageResult<Movie>>({
    url: `/movies/category/${tag}`,
    method: 'get',
    params: { page, size }
  });
}

/**
 * 获取电影详情
 */
export function getMovieDetailApi(id: number | string) {
  return request<Movie>({
    url: `/movies/${id}`,
    method: 'get'
  });
}

/**
 * 获取电影排片场次 (按影院分组)
 */
export function getMovieSchedulesApi(movieId: number | string) {
  return request<Array<{ cinema: Cinema; schedules: Schedule[] }>>({
    url: `/schedules/movie/${movieId}`,
    method: 'get'
  });
}

/**
 * 切换电影收藏状态
 */
export function toggleFavoriteApi(userId: number, movieId: number) {
  return request<number[]>({
    url: '/favorites/toggle',
    method: 'post',
    data: { userId, movieId }
  });
}

/**
 * 获取用户收藏的电影ID列表
 */
export function getUserFavoritesApi(userId: number) {
  return request<number[]>({
    url: `/favorites/user/${userId}`,
    method: 'get'
  });
}

/**
 * 获取电影排行榜 (按评分)
 */
export function getMovieRankingApi() {
  return request<Movie[]>({
    url: '/movies/ranking',
    method: 'get'
  });
}

/**
 * 根据ID列表获取电影详情
 */
export function getMoviesByIdsApi(ids: number[]) {
  return request<Movie[]>({
    url: '/movies/list',
    method: 'post',
    data: ids
  });
}
