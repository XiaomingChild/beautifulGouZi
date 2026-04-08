export interface User {
  id: number;
  account: string;
  nickname: string;
  phone: string;
  avatarUrl: string;
  bio: string;
  createdAt: string;
  favoriteMovieIds?: number[]; // 用户收藏的电影ID列表
}

export interface Movie {
  id: number;
  title: string;
  englishTitle?: string;
  posterUrl: string;
  director: string;
  actors: string;
  genre: string;
  durationMinutes: number;
  releaseDate: string;
  synopsis: string;
  rating: number;
  isShowing: number;
}

export interface Cinema {
  id: number;
  name: string;
  address: string;
  city: string;
  district?: string;
  phone?: string;
  rating: number;
  imgUrl?: string;
  services?: string; // 逗号分隔的特色服务
}

export interface Hall {
  id: number;
  cinemaId: number;
  name: string;
  type: string; // 2D, 3D, IMAX, etc.
  rowCount: number;
  colCount: number;
}

export interface Schedule {
  id: number;
  movieId: number;
  cinemaId: number;
  hallId: number;
  showDate: string;
  startTime: string;
  endTime: string;
  price: number;
  languageVersion?: string;
  hall?: Hall;
  cinema?: Cinema;
  movie?: Movie;
}

export interface Order {
  id: number;
  orderNo: string;
  userId: number;
  scheduleId: number;
  totalPrice: number;
  seatInfo: string;
  ticketCode?: string; // 取票码
  status: number; // 0:待支付, 1:已支付, 2:已完成, 3:已取消
  createTime: string;
  payTime?: string;
  schedule?: Schedule;
}

export interface BookedSeat {
  id: number;
  scheduleId: number;
  orderId?: number;
  rowIndex: number;
  colIndex: number;
  status: number; // 1-已售, 2-锁定中
}

export interface Favorite {
  id: number;
  userId: number;
  movieId: number;
  createdAt: string;
  movie?: Movie;
}

export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
