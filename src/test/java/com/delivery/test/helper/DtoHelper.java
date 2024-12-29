package com.delivery.test.helper;

import static com.delivery.test.helper.TestHelper.getRandomId;

import com.delivery.adapter.ws.dto.ReserveSlotDto;

public class DtoHelper {

  public static ReserveSlotDto generateRandomReserveSlotDto() {
    return new ReserveSlotDto(getRandomId(), getRandomId());
  }

}
