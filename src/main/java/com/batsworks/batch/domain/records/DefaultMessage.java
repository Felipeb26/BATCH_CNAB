package com.batsworks.batch.domain.records;

import com.batsworks.batch.domain.enums.Status;

public record DefaultMessage(Boolean sucess, String message, Status status) {

    public DefaultMessage(String message, Status status) {
        this(true, message, status);
    }

    public DefaultMessage(Boolean sucess, String message) {
        this(sucess, message, Status.UNKNOWN);
    }


}
