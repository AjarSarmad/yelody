package com.pluton.yelody.utilities;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;

import jakarta.persistence.criteria.Join;

public class SongSpecifications {
    public static Specification<Song> hasGenre(Genre genre) {
        return (song, cq, cb) -> cb.equal(song.get("genre"), genre);
    }

    public static Specification<Song> hasKeyword(Keyword keyword) {
        return (song, cq, cb) -> cb.isMember(keyword, song.get("keywordlist"));
    }
    
    public static Specification<Song> hasName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Song> hasArtistName(String artistName) {
        return (root, query, cb) -> cb.like(root.get("artistName"), "%" + artistName + "%");
    }
    
    public static Specification<Song> hasRank(Integer rank) {
        return (root, query, cb) -> cb.equal(root.get("rank"), rank);
    }
    
    public static Specification<Song> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            Join<Song, Keyword> join = root.join("keywordlist");
            return cb.like(join.get("name"), "%" + keyword + "%");
        };
    }

    public static Specification<Song> hasGenre(String genre) {
        return (root, query, cb) -> cb.like(root.get("genre").get("type"), "%" + genre + "%");
    }
    
    public static Sort getSortOrder(String sortBy, String order) {
        Sort.Order sortOrder = (order.equalsIgnoreCase("desc")) ? 
                                Sort.Order.desc(sortBy) : 
                                Sort.Order.asc(sortBy);
        return Sort.by(sortOrder);
    }
}

