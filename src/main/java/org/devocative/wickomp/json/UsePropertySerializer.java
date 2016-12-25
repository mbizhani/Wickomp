package org.devocative.wickomp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.devocative.adroit.ObjectUtil;

import java.io.IOException;

public class UsePropertySerializer extends JsonSerializer<Object> implements ContextualSerializer {
	private String property;

	public UsePropertySerializer() {
	}

	public UsePropertySerializer(String property) {
		this.property = property;
	}

	@Override
	public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		Object propertyValue = ObjectUtil.getPropertyValue(o, property, false);
		jsonGenerator.writeObject(propertyValue);
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
		JsonUseProperty ann;

		if (beanProperty != null) {
			ann = beanProperty.getAnnotation(JsonUseProperty.class);
		} else {
			throw new RuntimeException("Apply UsePropertySerializer over a property getter");
		}

		String prop;
		if (ann != null) {
			prop = ann.value();
		} else {
			throw new RuntimeException("Add @JsonUseProperty over a property getter when using UsePropertySerializer");
		}

		return new UsePropertySerializer(prop);
	}
}
