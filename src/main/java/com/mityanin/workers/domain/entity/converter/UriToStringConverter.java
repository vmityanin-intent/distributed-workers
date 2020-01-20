package com.mityanin.workers.domain.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.net.URI;

@Converter
public class UriToStringConverter implements AttributeConverter<URI, String> {


    @Override
    public String convertToDatabaseColumn(URI uri) {
        return uri.toString();
    }

    @Override
    public URI convertToEntityAttribute(String str) {
        return URI.create(str);
    }


}
