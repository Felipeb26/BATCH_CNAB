package com.batsworks.batch.domain.records;

import com.batsworks.batch.domain.enums.CnabStatus;

public record DefaultMessage(String message, CnabStatus cnabStatus) {
}
