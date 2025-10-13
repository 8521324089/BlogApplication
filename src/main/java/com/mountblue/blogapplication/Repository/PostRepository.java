package com.mountblue.blogapplication.Repository;

import com.mountblue.blogapplication.Model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT DISTINCT p.author FROM Post p")
    List<String> findAllAuthors();

    @Query("""
                SELECT DISTINCT p FROM Post p 
                LEFT JOIN p.tags t 
                WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Post> searchByKeyword(Pageable pageable, String keyword);

    @Query("""
                SELECT DISTINCT p
                FROM Post p
                LEFT JOIN p.tags t
                WHERE
                  ( :keyword IS NULL OR :keyword = '' OR
                    LOWER(p.author)  LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(p.title)   LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(t.name)    LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                  AND ( :hasAuthors = false OR LOWER(p.author) IN :authors )
                  AND ( :hasTags = false OR LOWER(t.name) IN :tagNames )
                  AND p.createdAt >= COALESCE(:fromDate, p.createdAt)
                  AND p.createdAt <= COALESCE(:toDate, p.createdAt)
                  GROUP BY p.id
                      HAVING
                        ( :hasTags = false OR COUNT(DISTINCT LOWER(t.name)) = :tagCount )
                        AND ( :hasAuthors = false OR COUNT(DISTINCT LOWER(p.author)) = :authorCount )
            """)
    Page<Post> searchFilterSort(
            @Param("keyword") String keyword,
            @Param("authors") List<String> authors,
            @Param("tagNames") List<String> tagNames,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("hasAuthors") Boolean hasAuthors,
            @Param("hasTags") Boolean hasTags,
            @Param("tagCount") Long tagCount,
            @Param("authorCount") Long authorCount,
            Pageable pageable
    );
}

