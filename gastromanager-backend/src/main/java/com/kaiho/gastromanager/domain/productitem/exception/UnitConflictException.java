package com.kaiho.gastromanager.domain.productitem.exception;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;

public class UnitConflictException extends RuntimeException {
    public UnitConflictException(String objectName, Unit unit) {
        super("The ingredient " + objectName + " requires units to be expressed in " + unit);
    }
}
