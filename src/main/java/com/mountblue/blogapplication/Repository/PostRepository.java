package com.mountblue.blogapplication.Repository;

import com.mountblue.blogapplication.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT DISTINCT p.author FROM posts p")
    List<String> findAllAuthors();

    @Query("""
        SELECT DISTINCT p FROM posts p 
        LEFT JOIN p.tags t 
        WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Post> searchByKeyword(String keyword);

    @Query(value = """
    SELECT p.* FROM posts p
    LEFT JOIN post_tag pt ON p.id = pt.post_id
    LEFT JOIN tags t ON pt.tag_id = t.id
    WHERE
        (:keyword IS NULL OR :keyword = ''
         OR p.title ILIKE CONCAT('%', :keyword, '%')
         OR p.content ILIKE CONCAT('%', :keyword, '%')
         OR p.author ILIKE CONCAT('%', :keyword, '%')
         OR t.name ILIKE CONCAT('%', :keyword, '%'))
      AND (:author IS NULL OR :author = '' OR p.author = :author)
      AND (:tag IS NULL OR :tag = '' OR t.name = :tag)
      AND (:fromDate IS NULL OR :fromDate = '' OR p.created_at >= CAST(:fromDate AS timestamp))
      AND (:toDate IS NULL OR :toDate = '' OR p.created_at < CAST(:toDate AS timestamp) + INTERVAL '1 day')
    ORDER BY
        CASE WHEN :sort = 'asc' THEN p.created_at END ASC, 
        CASE WHEN :sort = 'desc' THEN p.created_at END DESC
    """,
            nativeQuery = true)
    List<Post> searchFilterSort(String keyword, List<String> author, List<String> tag, String fromDate, String toDate,String sort);
}
