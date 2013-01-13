package ru.khl.core.tournament;

import java.util.Date;

public class Tournament {

	private Integer id;

	private String name;

	private Date startDate;

	private Date endDate;

	private TournamentType type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public TournamentType getType() {
		return type;
	}

	public void setType(TournamentType type) {
		this.type = type;
	}
}
