package murraco.repository;

import murraco.model.filmRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface filmRepository extends JpaRepository<filmRes,Integer> {

    @Query(value = "Select * From testbked.event where user_id=:UID" ,nativeQuery = true)
    filmRes[] findByUserID(@Param("UID")Integer UserID);
    @Query(value = "Select * From testbked.event where id=:ID" ,nativeQuery = true)
    filmRes findById(@Param("ID")Long ReservationId);
    @Query(value = "Select * From testbked.event where movie_id=:MID" ,nativeQuery = true)
    filmRes[] findByMovieID(@Param("MID")Long MovieID);

}
