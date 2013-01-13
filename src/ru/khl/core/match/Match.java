package ru.khl.core.match;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {

	private MatchTeamStatistics home = new MatchTeamStatistics();

	private MatchTeamStatistics away = new MatchTeamStatistics();

	private int number;

	private Integer id;

	private Date date;

	public Match() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public MatchTeamStatistics getHome() {
		return home;
	}

	public void setHome(MatchTeamStatistics home) {
		this.home = home;
	}

	public MatchTeamStatistics getAway() {
		return away;
	}

	public void setAway(MatchTeamStatistics away) {
		this.away = away;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(number);
		parcel.writeInt(id);
		parcel.writeLong(date.getTime());
	}

	public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
		public Match createFromParcel(Parcel in) {
			return new Match(in);
		}

		public Match[] newArray(int size) {
			return new Match[size];
		}
	};

	// конструктор, считывающий данные из Parcel
	private Match(Parcel parcel) {
		number = parcel.readInt();
		id = parcel.readInt();
		date = new Date(parcel.readLong());
	}
}
