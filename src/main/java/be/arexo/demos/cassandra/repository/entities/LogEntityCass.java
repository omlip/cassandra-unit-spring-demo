package be.arexo.demos.cassandra.repository.entities;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;

@Table(value = "logs")
public class LogEntityCass {

	@PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String id;

	@Column(value = "query")
	private String query;

	@Column(value = "date")
	private Date date;

	public LogEntityCass() {
		super();
	}

	public LogEntityCass(String query, Date date) {
		super();
		this.query = query;
		this.date = date;
	}

	public String getId() {
		return id;
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
		return "LogEntityCass [id=" + id + ", query=" + query + ", date=" + date + "]";
	}
}
