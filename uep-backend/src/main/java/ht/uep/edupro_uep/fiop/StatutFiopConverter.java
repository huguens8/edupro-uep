package ht.uep.edupro_uep.fiop;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatutFiopConverter implements AttributeConverter<StatutFiop, String> {

    @Override
    public String convertToDatabaseColumn(StatutFiop statut) {
        return statut == null ? null : statut.getDbValue();
    }

    @Override
    public StatutFiop convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : StatutFiop.fromDbValue(dbValue);
    }
}
