package com.batsworks.batch.domain.records;

import com.batsworks.batch.domain.enums.Status;

public record DefaultMessage(String message, Status status) {
}
