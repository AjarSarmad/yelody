package com.pluton.yelody.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.CombinedStatsResponse;
import com.pluton.yelody.repositories.BannerRepository;
import com.pluton.yelody.repositories.ChartRepository;
import com.pluton.yelody.repositories.SongRepository;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.repositories.UserSingHistoryRepository;
import com.pluton.yelody.services.CombinedStats;

@Service
public class CombinedStatsImpl implements CombinedStats {
	 	@Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private SongRepository songRepository;

	    @Autowired
	    private BannerRepository bannerRepository;

	    @Autowired
	    private ChartRepository chartRepository;

	    @Autowired
	    private UserSingHistoryRepository singHistoryRepository;
	    
	    @Override
	    public CombinedStatsResponse getCombinedStats() {
	    	CombinedStatsResponse stats = new CombinedStatsResponse();
	        stats.setTotalUsers(userRepository.countBy());
	        stats.setTotalSongs(songRepository.countBy());
	        stats.setTotalBanners(bannerRepository.countBy());
	        stats.setTotalCharts(chartRepository.countBy());
	        stats.setTotalSingHistory(singHistoryRepository.countBy());
	        return stats;
	    }
}
