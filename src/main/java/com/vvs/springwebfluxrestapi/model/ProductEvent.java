package com.vvs.springwebfluxrestapi.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
  
  @Id
  private Long eventId;
  private String eventType;
}
