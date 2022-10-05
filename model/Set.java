package basis.model;

import java.util.Objects;

public class Set {
	
	private Double weight;
	private Integer reps;
	
	public Double getWeight() {
		return weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	public Integer getReps() {
		return reps;
	}
	
	public void setReps(Integer reps) {
		this.reps = reps;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reps, weight);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Set other = (Set) obj;
		
		return Objects.equals(reps, other.reps) && Objects.equals(weight, other.weight);
	}

	@Override
	public String toString() {
		return "Set [weight=" + weight + ", reps=" + reps + "]";
	}
}
