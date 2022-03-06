package murraco.repository;

import murraco.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    @Query(value = "Select * From testbked.movie" ,nativeQuery = true)
    public Movie[] findAllMovies();
    @Query(value = "Select * From testbked.movie where title=:Title" ,nativeQuery = true)
    Optional<Movie> findMovieByTitle(@Param("Title")String Title);
    @Query(value = "SELECT * FROM testbked.movie where id=:ID" ,nativeQuery = true)
    Optional<Movie> findByID(@Param("ID")Long ID);
}