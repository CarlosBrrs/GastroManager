package com.kaiho.gastromanager.domain.order.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChangeOrderStatus {

    private String reason;
    private OperationalStatus newStatus;
}
