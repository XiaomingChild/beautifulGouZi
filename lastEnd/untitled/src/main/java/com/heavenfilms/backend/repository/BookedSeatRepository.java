package com.heavenfilms.backend.repository;

import com.heavenfilms.backend.entity.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    // 根据场次ID查找所有已售/锁定座位，用于渲染选座图
    List<BookedSeat> findByScheduleId(Long scheduleId);
    
    // 检查具体座位是否已被占用 (原子性检查依据)
    boolean existsByScheduleIdAndRowIndexAndColIndex(Long scheduleId, Integer rowIndex, Integer colIndex);
}
