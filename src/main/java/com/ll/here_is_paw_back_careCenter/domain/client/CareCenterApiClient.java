package com.ll.here_is_paw_back_careCenter.domain.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ll.here_is_paw_back_careCenter.domain.dto.response.CareCenterListResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@Slf4j
@Component
public class CareCenterApiClient {
  @Value("${api.public-data.key}")
  private String serviceKey;

  @Value("${api.public-data.baseUrl}")
  private String baseUrl;

  public CareCenterListResponse getCareCenters(int pageNo, int numOfRows) {
    try {
      // URL 생성
      String urlStr = baseUrl +
          "?serviceKey=" + serviceKey +
          "&pageNo=" + pageNo +
          "&numOfRows=" + numOfRows +
          "&_type=json";

      log.info("Requesting URL: {}", urlStr);

      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      // 요청 설정
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(5000);

      // 응답 코드 확인
      int responseCode = conn.getResponseCode();
      log.info("응답 코드: {}", responseCode);

      // 응답 읽기
      BufferedReader in;
      if (responseCode >= 200 && responseCode < 300) {
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
      } else {
        in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
      }

      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      String responseBody = response.toString();
      log.info("API 응답 본문: {}", responseBody);

      // JSON 문자열을 CareCenterListResponse 객체로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      // LocalDate 타입 처리를 위한 설정
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      // 응답 JSON을 CareCenterListResponse 객체로 변환
      CareCenterListResponse careCenterListResponse = objectMapper.readValue(responseBody, CareCenterListResponse.class);

      log.info("CareCenterListResponse: {}", careCenterListResponse.getResponse().getBody().getItems().getItem().get(0).getCareNm());
      return careCenterListResponse;
    } catch (Exception e) {
      log.error("API 호출 중 오류 발생", e);
      throw new RuntimeException("API 호출 실패", e);
    }
  }
}
