package com.ll.here_is_paw_back_careCenter.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "care_centers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareCenter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "care_nm", unique = true)
  private String careNm; // 동물보호센터명

  @Column(name = "org_nm")
  private String orgNm; // 관리기관명

  @Column(name = "division_nm")
  private String divisionNm; // 동물보호센터유형

  @Column(name = "save_trgt_animal")
  private String saveTrgtAnimal; // 구조대상동물

  @Column(name = "care_addr")
  private String careAddr; // 소재지도로명주소

  @Column(name = "jibun_addr")
  private String jibunAddr; // 소재지번주소

  @Column(name = "geo")
  private Point geo; // 위도, 경도

  @Column(name = "dsignation_date")
  private LocalDate dsignationDate; // 동물보호센터지정일자

  @Column(name = "week_opr_stime")
  private String weekOprStime; // 평일운영시작시각

  @Column(name = "week_opr_etime")
  private String weekOprEtime; // 평일운영종료시각

  @Column(name = "week_cell_stime")
  private String weekCellStime; // 평일분양시작시각

  @Column(name = "week_cell_etime")
  private String weekCellEtime; // 평일분양종료시각

  @Column(name = "weekend_opr_stime")
  private String weekendOprStime; // 주말운영시작시각

  @Column(name = "weekend_opr_etime")
  private String weekendOprEtime; // 주말운영종료시각

  @Column(name = "weekend_cell_stime")
  private String weekendCellStime; // 주말분양시작시각

  @Column(name = "weekend_cell_etime")
  private String weekendCellEtime; // 주말분양종료시각

  @Column(name = "close_day")
  private String closeDay; // 휴무일

  @Column(name = "vet_person_cnt")
  private Integer vetPersonCnt; // 수의사인원수

  @Column(name = "specs_person_cnt")
  private Integer specsPersonCnt; // 사양관리사인원수

  @Column(name = "medical_cnt")
  private Integer medicalCnt; // 진료실수

  @Column(name = "breed_cnt")
  private Integer breedCnt; // 사육실수

  @Column(name = "quarabtine_cnt")
  private Integer quarabtineCnt; // 격리실수

  @Column(name = "feed_cnt")
  private Integer feedCnt; // 사료보관실수

  @Column(name = "trans_car_cnt")
  private Integer transCarCnt; // 구조운반용차량보유대수

  @Column(name = "care_tel")
  private String careTel; // 전화번호

  @Column(name = "data_std_dt")
  private LocalDate dataStdDt; // 데이터기준일자
}
