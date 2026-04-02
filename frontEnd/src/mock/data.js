export const movies = [
  {
    id: 1,
    title: '奥本海默',
    enName: 'Oppenheimer',
    rating: '9.2',
    tags: ['剧情', '传记', '历史'],
    duration: 180,
    releaseDate: '2023-08-30',
    imgUrl: 'https://image.tmdb.org/t/p/w500/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg',
    bannerUrl: 'https://image.tmdb.org/t/p/original/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg',
    description: '克里斯托弗·诺兰自编自导的传记电影，聚焦“原子弹之父”罗伯特·奥本海默。故事背景设定在二战期间，物理学家罗伯特·奥本海默受命领导曼哈顿计划，在实验室研制原子弹，其过程不仅改变了战争进程，也永远改变了世界。',
    cast: [
      { name: '基里安·墨菲', role: '罗伯特·奥本海默', avatar: 'https://image.tmdb.org/t/p/w200/i90v9mOatfUvV8U89F07S9vP69U.jpg' },
      { name: '艾米莉·布朗特', role: '凯瑟琳·奥本海默', avatar: 'https://image.tmdb.org/t/p/w200/nSbtS6YpXG68F69OAmY6Y0SId58.jpg' },
      { name: '小罗伯特·唐尼', role: '路易斯·斯特劳斯', avatar: 'https://image.tmdb.org/t/p/w200/1Ym9vyiqT2ikv3u9v9U29X3f9U.jpg' }
    ]
  },
  {
    id: 102,
    title: '哪吒2：魔童闹海',
    enName: 'Ne Zha 2',
    rating: '9.5',
    tags: ['动画', '奇幻', '冒险'],
    duration: 125,
    releaseDate: '2025-02-10',
    imgUrl: 'https://image.tmdb.org/t/p/w500/4W0FnjSGn4x0mKZlBRx8OjFxQUM.jpg',
    bannerUrl: 'https://image.tmdb.org/t/p/original/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg',
    description: '天劫之后，哪吒、敖丙灵魂虽保住，但肉身很快会魂飞魄散。太乙真人打算用七彩宝莲给二人重塑肉身。但在重塑肉身的过程中却遇到重重困难，哪吒、敖丙的命运将走向何方？',
    cast: [
      { name: '吕艳婷', role: '哪吒', avatar: '' },
      { name: '瀚墨', role: '敖丙', avatar: '' }
    ]
  }
];

export const cinemas = [
  {
    id: 1,
    name: '万达影城（CBD店）',
    address: '朝阳区建国路93号万达广场三层',
    distance: '1.2km',
    tags: ['退票', '改签', '小吃', '杜比全景声']
  },
  {
    id: 2,
    name: 'CGV影城（北京旗舰店）',
    address: '朝阳区酒仙桥路18号颐堤港四层',
    distance: '3.5km',
    tags: ['退票', '4DX', 'IMAX']
  },
  {
    id: 3,
    name: '天堂影院（五道口店）',
    address: '海淀区成府路28号',
    distance: '5.8km',
    tags: ['退票', '改签', '巨幕']
  }
];

export const schedules = [
  {
    id: 1001,
    movieId: 1,
    cinemaId: 1,
    hall: '5号杜比厅',
    startTime: '10:30',
    endTime: '13:30',
    type: '原版 2D',
    price: 45.5,
    seats: [] // 实际应用中座位状态会实时变化
  },
  {
    id: 1002,
    movieId: 1,
    cinemaId: 1,
    hall: '2号激光厅',
    startTime: '14:20',
    endTime: '17:20',
    type: '原版 2D',
    price: 39.9,
    seats: []
  },
  {
    id: 1003,
    movieId: 1,
    cinemaId: 2,
    hall: 'IMAX厅',
    startTime: '19:00',
    endTime: '22:00',
    type: '原版 IMAX 2D',
    price: 88.0,
    seats: []
  }
];

export const userOrders = [
  {
    id: 'ORD202603310001',
    movieId: 1,
    movieTitle: '奥本海默',
    cinemaName: '万达影城（CBD店）',
    hall: '5号杜比厅',
    seats: ['3排5座', '3排6座'],
    price: 91.0,
    showTime: '2026-04-01 10:30',
    status: 'paid', // paid, unpaid, cancelled, refunded
    createTime: '2026-03-31 14:20'
  }
];
