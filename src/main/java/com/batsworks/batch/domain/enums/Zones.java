package com.batsworks.batch.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZoneId;

@Getter
@AllArgsConstructor
public enum Zones {

    AMERIACA_SAO_PAULO(ZoneId.of("America/Sao_Paulo"));
    private final ZoneId zone;

}
