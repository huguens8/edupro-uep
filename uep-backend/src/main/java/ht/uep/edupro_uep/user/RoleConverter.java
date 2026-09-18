package ht.uep.edupro_uep.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role == null ? null : role.getDbValue();
    }

    @Override
    public Role convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : Role.fromDbValue(dbValue);
    }
}
