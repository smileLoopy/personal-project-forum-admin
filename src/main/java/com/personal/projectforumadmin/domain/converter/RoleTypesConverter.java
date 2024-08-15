package com.personal.projectforumadmin.domain.converter;

import com.personal.projectforumadmin.domain.constant.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RoleTypesConverter implements AttributeConverter<Set<RoleType>, String> {

    private static final String DELIMITER = ",";

        @Override
        public String convertToDatabaseColumn(Set<RoleType> attribute) {
            return attribute.stream().map(RoleType::name).sorted().collect(Collectors.joining(DELIMITER));
        }

        @Override
        public Set<RoleType> convertToEntityAttribute(String dbData) {
            return Arrays.stream(dbData.split(DELIMITER)).map(RoleType::valueOf).collect(Collectors.toSet());
        }
}
