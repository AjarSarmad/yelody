package com.pluton.yelody.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinedStatsResponse {

    private Long totalUsers;
    private Long totalSongs;
    private Long totalBanners;
    private Long totalCharts;
    private Long totalSingHistory;

}
