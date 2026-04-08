package com.heavenfilms.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cinema_id", nullable = false)
    private Long cinemaId;

    @Column(nullable = false)
    private String name;

    private String type; // 影厅类型（2D, 3D, IMAX, 4DX）

    @Column(name = "row_count", nullable = false)
    private Integer rowCount;

    @Column(name = "col_count", nullable = false)
    private Integer colCount;
}
