package com.ll.here_is_paw_back_careCenter.domain.dto.response;

import com.ll.here_is_paw_back_careCenter.domain.entity.CareCenter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CareCenterMapResponse {

  private Long id;
  private String careNm; // 동물보호센터명
  private String careAddr; // 소재지도로명주소
  private String jibunAddr; // 소재지번주소
  private Double lat;
  private Double lng;
  private String weekOprStime; // 평일운영시작시각
  private String weekOprEtime; // 평일운영종료시각
  private String weekendOprStime; // 주말운영시작시각
  private String weekendOprEtime; // 주말운영종료시각
  private String closeDay; // 휴무일
  private String careTel; // 전화번호

  public static CareCenterMapResponse of(CareCenter careCenter){
    Double latitude = null;
    Double longitude = null;

    if (careCenter.getGeo() != null) {
      latitude = careCenter.getGeo().getX();
      longitude = careCenter.getGeo().getY();
    }

    return CareCenterMapResponse.builder()
        .id(careCenter.getId())
        .careNm(careCenter.getCareNm())
        .careAddr(careCenter.getCareAddr())
        .jibunAddr(careCenter.getJibunAddr())
        .lat(latitude)
        .lng(longitude)
        .weekOprStime(careCenter.getWeekOprStime())
        .weekOprEtime(careCenter.getWeekOprEtime())
        .weekendOprStime(careCenter.getWeekendOprStime())
        .weekendOprEtime(careCenter.getWeekendOprEtime())
        .closeDay(careCenter.getCloseDay())
        .careTel(careCenter.getCareTel())
        .build();
  }
}
