package basis.model;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;


public class WorkoutDetails {
	
	private Workout workout;
	private List<ExerciseSet> exerciseSets = new ArrayList<>();
	
	public Workout getWorkout()	{
		return workout;
	}
	
	public void setWorkout(Workout workout)	{
		this.workout = workout;
	}
	
	public List<ExerciseSet> getExerciseSets() {
		return exerciseSets;
	}
	
	public void setExerciseSets(List<ExerciseSet> exerciseSets)	{
		this.exerciseSets = exerciseSets;
	}
	
	public void addExerciseSet(ExerciseSet exerciseSet)	{
		exerciseSets.add(exerciseSet);
	}
	
	public void removeExerciseSet(ExerciseSet exerciseSet) {
		exerciseSets.remove(exerciseSet);
	}

	@Override
	public int hashCode() {
		return Objects.hash(exerciseSets, workout);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		WorkoutDetails other = (WorkoutDetails) obj;
		
		return Objects.equals(exerciseSets, other.exerciseSets) && Objects.equals(workout, other.workout);
	}

	@Override
	public String toString() {
		return "WorkoutDetails [workout=" + workout + ", exerciseSets=" + exerciseSets + "]";
	}
}
