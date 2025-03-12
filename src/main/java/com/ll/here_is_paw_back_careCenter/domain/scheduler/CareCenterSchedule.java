package com.ll.here_is_paw_back_careCenter.domain.scheduler;

import com.ll.here_is_paw_back_careCenter.domain.service.CareCenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CareCenterSchedule {

  private final CareCenterService careCenterService;

  @Scheduled(cron = "0 0 12 * * ?")
  public void updateCareCenter() {
    log.info("동물보호센터 정보 일일 업데이트 시작");
    careCenterService.fetchAllCareCenters(100);  // 페이지당 100개 항목 가져오기
    log.info("동물보호센터 정보 일일 업데이트 완료");
  }

}