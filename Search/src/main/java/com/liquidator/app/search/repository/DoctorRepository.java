package com.liquidator.app.search.repository;

import com.liquidator.app.search.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.speciality) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY d.review DESC")
    List<Doctor> searchBySpeciality(@Param("query") String query);

}

