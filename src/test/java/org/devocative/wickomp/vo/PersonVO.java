package org.devocative.wickomp.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PersonVO implements Serializable {
	private String col01;
	private String col02;
	private String col03;
	private String col04;
	private String col05;
	private Date birthDate;
	private Long income;
	private boolean alive;

	public PersonVO() {
	}

	public String getCol01() {
		return col01;
	}

	public PersonVO setCol01(String col01) {
		this.col01 = col01;
		return this;
	}

	public String getCol02() {
		return col02;
	}

	public PersonVO setCol02(String col02) {
		this.col02 = col02;
		return this;
	}

	public String getCol03() {
		return col03;
	}

	public PersonVO setCol03(String col03) {
		this.col03 = col03;
		return this;
	}

	public String getCol04() {
		return col04;
	}

	public PersonVO setCol04(String col04) {
		this.col04 = col04;
		return this;
	}

	public String getCol05() {
		return col05;
	}

	public PersonVO setCol05(String col05) {
		this.col05 = col05;
		return this;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public PersonVO setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	public Long getIncome() {
		return income;
	}

	public PersonVO setIncome(Long income) {
		this.income = income;
		return this;
	}

	public boolean isAlive() {
		return alive;
	}

	public PersonVO setAlive(boolean alive) {
		this.alive = alive;
		return this;
	}

	@Override
	public String toString() {
		return col02;
	}

	public static List<PersonVO> list() {
		long time = new Date().getTime();

		List<PersonVO> list = new ArrayList<PersonVO>();
		for (int i = 0; i < 189; i++) {
			list.add(new PersonVO()
					.setCol01((i / 7) + "")
					.setCol02(i + " - " + UUID.randomUUID().toString())
					.setCol03("سلام world خوبی؟")
					.setCol04("C4 " + UUID.randomUUID().toString())
					.setCol05("C5 " + UUID.randomUUID().toString())
					.setBirthDate(new Timestamp((long) (time - Math.random() * 99999999999L)))
					.setIncome((long) (Math.random() * 100000000) * (i % 3 == 0 ? -1 : 1))
					.setAlive(((int) (Math.random() * 1000)) % 7 == 0)
			);
		}
		return list;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PersonVO)) return false;

		PersonVO personVO = (PersonVO) o;

		return !(getCol02() != null ? !getCol02().equals(personVO.getCol02()) : personVO.getCol02() != null);

	}

	@Override
	public int hashCode() {
		return getCol02() != null ? getCol02().hashCode() : 0;
	}
}
