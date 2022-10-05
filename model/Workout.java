package basis.model;

import java.sql.Date;
import java.util.Objects;

public class Workout {
	
	private Integer id;
	private Date date;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Workout other = (Workout) obj;
		
		return Objects.equals(date, other.date);
	}

	@Override
	public String toString() {
		return "Workout [id=" + id + ", date=" + date + "]";
	}
}
