package basis.model;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

public class ExerciseSet {
	
	private Exercise exercise;
	private List<Set> sets = new ArrayList<>();
	
	public Exercise getExercise() {
		return exercise;
	}
	
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}
	
	public List<Set> getSets() {
		return sets;
	}
	
	public void setSets(List<Set> sets) {
		this.sets = sets;
	}
	
	public void addSet(Set set)	{
		sets.add(set);
	}
	
	public void removeSet(Set set) {
		sets.remove(set);
	}

	@Override
	public int hashCode() {
		return Objects.hash(exercise, sets);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		ExerciseSet other = (ExerciseSet) obj;
		
		return Objects.equals(exercise, other.exercise) && Objects.equals(sets, other.sets);
	}

	@Override
	public String toString() {
		return "ExerciseSet [exercise=" + exercise + ", sets=" + sets + "]";
	}
}
