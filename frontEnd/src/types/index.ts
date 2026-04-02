export interface User {
  id: number;
  account: string;
  nickname: string;
  phone: string;
  avatar: string;
  bio: string;
  selected: number[];
  token?: string;
}

export interface Movie {
  id: number;
  title: string;
  posterUrl: string;
  director: string;
  cast: string;
  genre: string;
  durationMinutes: number;
  releaseDate: string;
  synopsis: string;
  rating: number;
}

export interface Cinema {
  id: number;
  name: string;
  address: string;
  tags?: string[];
  distance?: string;
}

export interface Schedule {
  id: number;
  movieId: number;
  cinemaId: number;
  hallName: string;
  startTime: string;
  endTime: string;
  price: number;
}

export interface Order {
  id: string;
  userId: number;
  scheduleId: number;
  seats: string;
  totalPrice: number;
  status: number;
  createdAt: string;
}

export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
