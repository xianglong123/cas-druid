package com.cas.config.factory;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 * @author xianglong
 */
@Component
@ConfigurationPropertiesBinding
public class ObjectFactoryConverter implements Converter<String, ObjectFactory> {

    @Override
    public ObjectFactory convert(String source) {
        try {
            return (ObjectFactory) Class.forName(source).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
