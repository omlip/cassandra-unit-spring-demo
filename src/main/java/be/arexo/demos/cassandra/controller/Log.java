package be.arexo.demos.cassandra.controller;

import java.util.Date;
import java.util.UUID;

public class Log {

	private String id;

	private String query;

	private Date date;

	public Log() {
		super();
	}

	public Log(String query, Date date) {
		super();
		this.query = query;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", query=" + query + ", date=" + date + "]";
	}
}
