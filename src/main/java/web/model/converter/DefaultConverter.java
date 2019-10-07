package web.model.converter;

import com.github.dozermapper.core.CustomConverter;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

// TODO this only exists because there is a bug with the dozer mappings
public class DefaultConverter implements CustomConverter {

    private static final Mapper mapper = DozerBeanMapperBuilder.create().withMappingFiles("config/dozer_mappings.xml").build();

    @Override
    public Object convert(Object o, Object o1, Class<?> aClass, Class<?> aClass1) {
        if (o1 == null) {
            return null;
        }
        if (o == null) {
            try {
                o = aClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        mapper.map(o1, o);
        return o;
    }
}
