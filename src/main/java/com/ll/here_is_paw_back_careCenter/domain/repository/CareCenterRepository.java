package com.ll.here_is_paw_back_careCenter.domain.repository;

import com.ll.here_is_paw_back_careCenter.domain.entity.CareCenter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CareCenterRepository extends JpaRepository<CareCenter, Long> {

  Optional<CareCenter> findByCareNm(String careNm);

  @Query(value = "SELECT c.* FROM care_centers c " +
      "WHERE ST_DWithin(c.geo, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326), :radius, true) " +
      "ORDER BY ST_Distance(c.geo, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326))",
      nativeQuery = true)
  List<CareCenter> findWithinRadius(@Param("lat") double lat,
      @Param("lng") double lng,
      @Param("radius") double radius);
}
