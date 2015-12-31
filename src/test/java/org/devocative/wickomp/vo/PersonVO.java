package org.devocative.wickomp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonVO implements Serializable {
	private String col01;
	private String col02;
	private String col03;
	private String col04;
	private String col05;

	public PersonVO() {
	}

	public PersonVO(String col01, String col02, String col03, String col04, String col05) {
		this.col01 = col01;
		this.col02 = col02;
		this.col03 = col03;
		this.col04 = col04;
		this.col05 = col05;
	}

	public String getCol01() {
		return col01;
	}

	public void setCol01(String col01) {
		this.col01 = col01;
	}

	public String getCol02() {
		return col02;
	}

	public void setCol02(String col02) {
		this.col02 = col02;
	}

	public String getCol03() {
		return col03;
	}

	public void setCol03(String col03) {
		this.col03 = col03;
	}

	public String getCol04() {
		return col04;
	}

	public void setCol04(String col04) {
		this.col04 = col04;
	}

	public String getCol05() {
		return col05;
	}

	public void setCol05(String col05) {
		this.col05 = col05;
	}

	@Override
	public String toString() {
		return "PersonVO{" +
			"col01='" + col01 + '\'' +
			'}';
	}

	public static List<PersonVO> list() {
		List<PersonVO> list = new ArrayList<PersonVO>();
		for (int i = 0; i < 189; i++) {
			list.add(new PersonVO(
				(i / 7) + "",
				UUID.randomUUID().toString(),
				"مهدی بیژنی oskol هستم",
				UUID.randomUUID().toString(),
				UUID.randomUUID().toString()
			));
		}
		return list;
	}
}
