package org.devocative.wickomp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeVO implements Serializable {
	private String id;
	private String name;
	private Integer age;
	private String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getParentId() {
		return parentId;
	}

	public EmployeeVO setParentId(String parentId) {
		this.parentId = parentId;
		return this;
	}

	private static List<EmployeeVO> employeeVOs = new ArrayList<>();

	static {
		for (int i = 1; i <= 10; i++) {
			EmployeeVO e = new EmployeeVO();
			e.setId(String.valueOf(i));
			e.setName(String.format("M%03d", i));
			e.setAge((int) (Math.random() * 100));
			employeeVOs.add(e);
		}

		for (int i = 50; i <= 100; i++) {
			EmployeeVO e = new EmployeeVO();
			e.setId(String.valueOf(i));
			e.setName(String.format("M%03d", i));
			e.setAge((int) (Math.random() * 100));

			int parentId = (int) (Math.random() * 10) + 1;
			e.setParentId(String.valueOf(parentId));
			employeeVOs.add(e);
		}
	}

	public static List<EmployeeVO> list() {
		return employeeVOs;
	}
}
