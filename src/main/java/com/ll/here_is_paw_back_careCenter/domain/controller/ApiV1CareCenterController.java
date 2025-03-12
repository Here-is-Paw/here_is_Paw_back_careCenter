package com.ll.here_is_paw_back_careCenter.domain.controller;

import com.ll.here_is_paw_back_careCenter.domain.dto.response.CareCenterMapResponse;
import com.ll.here_is_paw_back_careCenter.domain.service.CareCenterService;
import com.ll.here_is_paw_back_careCenter.global.globalDto.GlobalResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-center")
public class ApiV1CareCenterController {

  private final CareCenterService careCenterService;

  @GetMapping("/radius")
  public GlobalResponse<List<CareCenterMapResponse>> radiusList(
      @RequestParam("latitude") Double lat,
      @RequestParam("longitude") Double lng,
      @RequestParam("radius") Double radius
  ) {
    return GlobalResponse.success(careCenterService.radiusList(lat, lng, radius));
  }
}
