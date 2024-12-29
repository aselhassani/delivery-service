package com.delivery;

import com.delivery.adapter.ws.dto.DeliveryDto;
import com.delivery.adapter.ws.dto.DeliveryMode;
import com.delivery.adapter.ws.dto.DeliverySlotDto;
import com.delivery.adapter.ws.dto.ReserveSlotDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ApiClient {

  private final ObjectMapper objectMapper;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  public DeliverySlotDto getDeliverySlot(DeliveryMode deliveryMode) {

    try {
      var uri = URI.create("http://localhost:8042/slots/" + deliveryMode);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(uri)
          .headers("content-type", "application/json")
          .GET()
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      return objectMapper.readValue(response.body(),  new TypeReference<List<DeliverySlotDto>>() {}).getLast();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public DeliveryDto reserveDeliverySlot(String orderId, String slotId) {

    try {
      var json = objectMapper.writeValueAsString(new ReserveSlotDto(orderId, slotId));

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("http://localhost:8042/delivery"))
          .headers("content-type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      return objectMapper.readValue(response.body(), DeliveryDto.class);

    } catch (Exception e){
      throw new RuntimeException(e);
    }

  }


}
