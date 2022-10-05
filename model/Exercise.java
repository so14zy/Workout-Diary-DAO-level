package basis.model;

import java.util.Objects;

public class Exercise {
	
	private Integer id;
	private String title;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Exercise other = (Exercise) obj;
		
		return Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", title=" + title + "]";
	}
}
