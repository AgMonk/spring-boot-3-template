package com.gin.springboot3template.sys.jpaconverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * * JPA 字段上需要添加注解 @ElementCollection
 * * 并指定使用的转换器： @Convert(converter = JpaConverterListInt.class)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/10 16:32
 */
@Converter
public class JpaConverterListInt implements AttributeConverter<List<Integer>, String> {
    public static final String DELIMITER = ",";

    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return attribute == null ? null : attribute.stream().map(String::valueOf).collect(Collectors.joining(DELIMITER));
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct <code>dbData</code> type for the corresponding
     * column for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     * @param dbData the data from the database column to be
     *               converted
     * @return the converted value to be stored in the entity
     * attribute
     */
    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Arrays.stream(dbData.split(DELIMITER)).map(Integer::parseInt).toList();
    }
}
