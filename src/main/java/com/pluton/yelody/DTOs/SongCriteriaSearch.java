package com.pluton.yelody.DTOs;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongCriteriaSearch {
    private String name;
    private String artistName;
    private Integer rank;
    private String genre;
    private String keyword;

    @Pattern(regexp = "^(asc|desc)$", message = "Invalid order value")
    private String order; 

    @Pattern(regexp = "^(name|artistName|rank|genre|keyword)$", message = "Invalid sortBy value")
    private String sortBy;

    public boolean hasFilters() {
        return name != null || artistName != null || rank != null || genre != null || keyword != null;
    }

}
