package com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.mapper;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.entity.TaxConfigEntity;
import org.springframework.stereotype.Component;

@Component
public class TaxConfigEntityMapper {
    public TaxConfig toDomain(TaxConfigEntity taxConfigEntity) {
        if (taxConfigEntity == null) {
            return null;
        }
        return TaxConfig.builder()
                        .uuid(taxConfigEntity.getUuid())
                        .taxType(taxConfigEntity.getTaxType())
                        .taxRate(taxConfigEntity.getTaxRate())
                        .description(taxConfigEntity.getDescription())
                        .createdBy(taxConfigEntity.getCreatedBy())
                        .createdDate(taxConfigEntity.getCreatedDate())
                        .updatedBy(taxConfigEntity.getUpdatedBy())
                        .updatedDate(taxConfigEntity.getUpdatedDate())
                        .build();
    }
}
