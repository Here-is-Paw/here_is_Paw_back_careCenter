package com.ll.here_is_paw_back_careCenter.domain.service;


import com.ll.here_is_paw_back_careCenter.domain.client.CareCenterApiClient;
import com.ll.here_is_paw_back_careCenter.domain.dto.response.CareCenterListResponse;
import com.ll.here_is_paw_back_careCenter.domain.dto.response.CareCenterListResponse.CareCenterDto;
import com.ll.here_is_paw_back_careCenter.domain.dto.response.CareCenterMapResponse;
import com.ll.here_is_paw_back_careCenter.domain.entity.CareCenter;
import com.ll.here_is_paw_back_careCenter.domain.repository.CareCenterRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CareCenterService {

  private final CareCenterRepository careCenterRepository;
  private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); // SRID 4326 = WGS84

  // API 호출 메소드는 이미 구현되어 있다고 가정합니다.
  private final CareCenterApiClient careCenterApiClient;

  @Transactional
  public void fetchAndSaveCareCenters(int pageNo, int numOfRows) {
    try {
      // API 호출
      CareCenterListResponse response = careCenterApiClient.getCareCenters(pageNo, numOfRows);

      // 응답 확인
      if (response == null || response.getResponse() == null || response.getResponse().getBody() == null
          || response.getResponse().getBody().getItems() == null
          || response.getResponse().getBody().getItems().getItem() == null) {
        log.error("API 응답에 필요한 데이터가 없습니다.");
        return;
      }

      List<CareCenterDto> careCenterDtos = response.getResponse().getBody().getItems().getItem();
      log.info("보호센터 정보 {} 건을 가져왔습니다.", careCenterDtos.size());

      int newCount = 0;
      int updateCount = 0;

      // 각 DTO를 처리
      for (CareCenterDto dto : careCenterDtos) {
        boolean isNew = saveOrUpdateCareCenter(dto);
        if (isNew) {
          newCount++;
        } else {
          updateCount++;
        }
      }

      log.info("보호센터 정보 처리 완료 - 신규: {} 건, 업데이트: {} 건", newCount, updateCount);

    } catch (Exception e) {
      log.error("보호센터 정보 가져오기 및 저장 중 오류 발생", e);
      throw new RuntimeException("보호센터 정보 처리 실패", e);
    }
  }

  private boolean saveOrUpdateCareCenter(CareCenterDto dto) {
    // DTO를 엔티티로 변환
    CareCenter careCenter = convertDtoToEntity(dto);

    // 상호명으로 기존 데이터 조회
    Optional<CareCenter> existingCenter = careCenterRepository.findByCareNm(dto.getCareNm());

    // 기존 데이터가 있으면 ID 설정 (JPA가 자동으로 업데이트)
    if (existingCenter.isPresent()) {
      careCenter.setId(existingCenter.get().getId());
      careCenterRepository.save(careCenter);
      return false; // 업데이트됨
    } else {
      // 신규 등록
      careCenterRepository.save(careCenter);
      return true; // 신규 등록됨
    }
  }

  private CareCenter convertDtoToEntity(CareCenterDto dto) {
    // 위도, 경도를 포인트로 변환
    Point geoPoint = null;
    if (dto.getLat() != null && dto.getLng() != null) {
      geoPoint = geometryFactory.createPoint(new Coordinate(dto.getLng(), dto.getLat()));
    }

    return CareCenter.builder()
        .careNm(dto.getCareNm())
        .orgNm(dto.getOrgNm())
        .divisionNm(dto.getDivisionNm())
        .saveTrgtAnimal(dto.getSaveTrgtAnimal())
        .careAddr(dto.getCareAddr())
        .jibunAddr(dto.getJibunAddr())
        .geo(geoPoint)
        .dsignationDate(dto.getDsignationDate())
        .weekOprStime(dto.getWeekOprStime())
        .weekOprEtime(dto.getWeekOprEtime())
        .weekCellStime(dto.getWeekCellStime())
        .weekCellEtime(dto.getWeekCellEtime())
        .weekendOprStime(dto.getWeekendOprStime())
        .weekendOprEtime(dto.getWeekendOprEtime())
        .weekendCellStime(dto.getWeekendCellStime())
        .weekendCellEtime(dto.getWeekendCellEtime())
        .closeDay(dto.getCloseDay())
        .vetPersonCnt(dto.getVetPersonCnt())
        .specsPersonCnt(dto.getSpecsPersonCnt())
        .medicalCnt(dto.getMedicalCnt())
        .breedCnt(dto.getBreedCnt())
        .quarabtineCnt(dto.getQuarabtineCnt())
        .feedCnt(dto.getFeedCnt())
        .transCarCnt(dto.getTransCarCnt())
        .careTel(dto.getCareTel())
        .dataStdDt(dto.getDataStdDt())
        .build();
  }

  @Transactional
  public void fetchAllCareCenters(int numOfRows) {
    try {
      // 첫 페이지를 가져와서 총 건수 확인
      CareCenterListResponse firstPage = careCenterApiClient.getCareCenters(1, numOfRows);

      if (firstPage == null || firstPage.getResponse() == null || firstPage.getResponse().getBody() == null) {
        log.error("첫 페이지 응답에 필요한 데이터가 없습니다.");
        return;
      }

      int totalCount = firstPage.getResponse().getBody().getTotalCount();
      int totalPages = (int) Math.ceil((double) totalCount / numOfRows);

      log.info("총 {} 건, {} 페이지의 보호센터 정보를 가져옵니다.", totalCount, totalPages);

      // 첫 페이지 처리
      processCareCenterResponse(firstPage);

      // 나머지 페이지 처리
      for (int page = 2; page <= totalPages; page++) {
        CareCenterListResponse response = careCenterApiClient.getCareCenters(page, numOfRows);
        processCareCenterResponse(response);
      }

      log.info("모든 보호센터 정보를 성공적으로 가져와 저장했습니다.");

    } catch (Exception e) {
      log.error("전체 보호센터 정보 가져오기 및 저장 중 오류 발생", e);
      throw new RuntimeException("전체 보호센터 정보 처리 실패", e);
    }
  }

  private void processCareCenterResponse(CareCenterListResponse response) {
    if (response == null || response.getResponse() == null || response.getResponse().getBody() == null
        || response.getResponse().getBody().getItems() == null
        || response.getResponse().getBody().getItems().getItem() == null) {
      log.warn("API 응답에 필요한 데이터가 없습니다.");
      return;
    }

    List<CareCenterDto> careCenterDtos = response.getResponse().getBody().getItems().getItem();

    int newCount = 0;
    int updateCount = 0;

    for (CareCenterDto dto : careCenterDtos) {
      boolean isNew = saveOrUpdateCareCenter(dto);
      if (isNew) {
        newCount++;
      } else {
        updateCount++;
      }
    }

    log.info("페이지 처리 완료 - 신규: {} 건, 업데이트: {} 건", newCount, updateCount);
  }

  public List<CareCenterMapResponse> radiusList(Double lat, Double lng, Double radius) {
    List<CareCenter> careCenters = careCenterRepository.findWithinRadius(lat, lng, radius);
    return careCenters.stream().map(CareCenterMapResponse::of).toList();
  }
}