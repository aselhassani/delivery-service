package com.delivery.adapter.ws;

import com.delivery.adapter.ws.dto.DeliveryMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeliveryModeControllerTest {

  @InjectMocks
  private DeliveryModeController underTest;

  @Test
  void getDeliveryModesShouldRetrieveAllDeliveryModes() {

    var result = underTest.getDeliveryModes();

    assertThat(result)
      .isNotNull()
      .satisfies(res -> {
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody())
          .containsExactlyInAnyOrder(
            Arrays.stream(DeliveryMode.values())
              .map(Enum::name)
              .toArray(String[]::new)
          );
      });

  }

}
