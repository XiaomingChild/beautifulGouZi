import request from '../utils/request';

/**
 * 获取正在热映电影
 */
export function getHotMoviesApi(genre: string = 'all') {
  return request({
    url: '/movies/hot',
    method: 'get',
    params: { genre }
  });
}

/**
 * 获取即将上映电影
 */
export function getUpcomingMoviesApi(genre: string = 'all') {
  return request({
    url: '/movies/upcoming',
    method: 'get',
    params: { genre }
  });
}

/**
 * 根据分类获取电影
 */
export function getMoviesByCategoryApi(tag: string) {
  return request({
    url: `/movies/category/${tag}`,
    method: 'get'
  });
}

/**
 * 获取电影详情
 */
export function getMovieDetailApi(id: number | string) {
  return request({
    url: `/movies/${id}`,
    method: 'get'
  });
}

/**
 * 获取电影排片场次
 */
export function getMovieSchedulesApi(movieId: number | string) {
  return request({
    url: `/schedules/movie/${movieId}`,
    method: 'get'
  });
}

/**
 * 切换电影收藏状态
 */
export function toggleFavoriteApi(userId: number, movieId: number) {
  return request({
    url: '/favorites/toggle',
    method: 'post',
    data: { userId, movieId }
  });
}

/**
 * 获取用户收藏的电影ID列表
 */
export function getUserFavoritesApi(userId: number) {
  return request({
    url: `/favorites/user/${userId}`,
    method: 'get'
  });
}

/**
 * 获取电影排行榜 (按评分)
 */
export function getMovieRankingApi() {
  return request({
    url: '/movies/ranking',
    method: 'get'
  });
}

/**
 * 根据ID列表获取电影详情
 */
export function getMoviesByIdsApi(ids: number[]) {
  return request({
    url: '/movies/list',
    method: 'post',
    data: ids
  });
}
