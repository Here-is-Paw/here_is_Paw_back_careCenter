package com.ll.here_is_paw_back_careCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HereIsPawBackHospitalApplication {

//  @Autowired
//  private CareCenterService careCenterService; // 서비스 주입

  public static void main(String[] args) {
    SpringApplication.run(HereIsPawBackHospitalApplication.class, args);
  }

//  @Bean
//  public CommandLineRunner runner() {
//    return args -> {
//      careCenterService.fetchAllCareCenters(100);
//    };
//  }
}