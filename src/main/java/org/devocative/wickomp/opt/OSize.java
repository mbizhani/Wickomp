package org.devocative.wickomp.opt;

import com.fasterxml.jackson.annotation.JsonValue;

public class OSize extends Options {
	public enum EType {Percent, Fixed}

	private Integer size;
	private EType type;

	private OSize(Integer size, EType type) {
		this.size = size;
		this.type = type;
	}

	@JsonValue
	public Object getValue() {
		switch (type) {
			case Percent:
				return size + "%";
			case Fixed:
				return size;
		}

		throw new RuntimeException("Wrong size type!");
	}

	public static OSize percent(int size) {
		if (size < 0 || size > 100)
			throw new RuntimeException("Invalid percent value: " + size);
		return new OSize(size, EType.Percent);
	}

	public static OSize fixed(int size) {
		return new OSize(size, EType.Fixed);
	}
}
