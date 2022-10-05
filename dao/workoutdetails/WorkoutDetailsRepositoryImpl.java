package basis.dao.workoutdetails;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import basis.dao.exception.DAOException;
import basis.util.exception.DBException;
import basis.model.Exercise;
import basis.model.ExerciseSet;
import basis.model.Set;
import basis.model.Workout;
import basis.model.WorkoutDetails;
import basis.util.DBConnector;


public class WorkoutDetailsRepositoryImpl implements WorkoutDetailsRepository {
	
	private final String SAVE_QUERY = "INSERT INTO workout_details "
			+ "(workout_id, exercise_id, set_num, weight, reps) "
			+ "VALUES (?, ?, ?, ?, ?)";
	
	private final String FIND_ALL_QUERY = "SELECT workout.id AS w_id, workout.date AS w_date, "
			+ "exercise.id AS e_id, exercise.title AS e_title, "
			+ "details.weight, details.reps "
			+ "FROM workout_details AS details "
			+ "INNER JOIN workout "
			+ "ON details.workout_id = workout.id "
			+ "INNER JOIN exercise "
			+ "ON details.exercise_id = exercise.id";
	
	private final String FIND_BY_ID_QUERY = "SELECT exercise.id AS e_id, exercise.title AS e_title, "
			+ "details.weight, details.reps "
			+ "FROM workout_details AS details "
			+ "INNER JOIN workout "
			+ "ON details.workout_id = workout.id "
			+ "INNER JOIN exercise "
			+ "ON details.exercise_id = exercise.id "
			+ "WHERE details.workout_id = ?";
	
	private final String FIND_BY_DATE_QUERY = "SELECT workout.id AS w_id, "
			+ "exercise.id AS e_id, exercise.title AS e_title, "
			+ "details.weight, details.reps "
			+ "FROM workout_details AS details "
			+ "INNER JOIN workout "
			+ "ON details.workout_id = workout.id "
			+ "INNER JOIN exercise "
			+ "ON details.exercise_id = exercise.id "
			+ "WHERE workout.date = ?";
	
	private final String FIND_BY_EXERCISE_TITLE_QUERY = "SELECT workout.id AS w_id, workout.date AS w_date, "
			+ "exercise.id AS e_id, "
			+ "details.weight, details.reps "
			+ "FROM workout_details AS details "
			+ "INNER JOIN workout "
			+ "ON details.workout_id = workout.id "
			+ "INNER JOIN exercise "
			+ "ON details.exercise_id = exercise.id "
			+ "WHERE exercise.title = ?";
	
	private final String DELETE_BY_ID_QUERY = "DELETE FROM workout_details "
			+ "WHERE workout_id = ?";

	@Override
	public boolean save(WorkoutDetails model) throws DAOException {
		boolean isSaved = false;
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)
				) {
			Integer workoutId = model
					.getWorkout()
					.getId();
			
			int i = 0;
			
			List<ExerciseSet> exerciseSets = model.getExerciseSets();
			for (ExerciseSet exerciseSet : exerciseSets) {
				
				Integer exerciseId = exerciseSet
						.getExercise()
						.getId();
				
				List<Set> sets = exerciseSet.getSets();
				int setNum = 0;
				for (Set set : sets) {
					
					statement.setInt(1, workoutId);
					statement.setInt(2, exerciseId);
					statement.setInt(3, ++setNum);
					statement.setDouble(4, set.getWeight());
					statement.setInt(5, set.getReps());
					
					i = i + statement.executeUpdate();
				}
			}
			
			if (i > 0)
				isSaved = true;
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to save workout details", e);
		}
		
		return isSaved;
	}

	@Override
	public Optional<WorkoutDetails> findById(Workout id) throws DAOException {
		
		Optional<WorkoutDetails> optDetails = Optional.empty();
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)
				) {
			
			statement.setInt(1, id.getId());
			ResultSet rs = statement.executeQuery();
			
			WorkoutDetails details = null;
			ExerciseSet exerciseSet = null;
			Exercise exercise = null;
			int tempExerciseId = -1;
			boolean wereDetailsCreated = false;
			while (rs.next()) {
				
				if (wereDetailsCreated == false) {
					
					details = new WorkoutDetails();
					details.setWorkout(id);
					
					wereDetailsCreated = true;
				}
				
				int exerciseId = rs.getInt("e_id");
				double weight = rs.getDouble("weight");
				int reps = rs.getInt("reps");
				
				Set set = new Set();
				set.setWeight(weight);
				set.setReps(reps);
				
				if (tempExerciseId != exerciseId) {
					exercise = new Exercise();
					exercise.setId(exerciseId);
					exercise.setTitle(rs.getString("e_title"));
					
					exerciseSet = new ExerciseSet();
					exerciseSet.setExercise(exercise);
					
					details.addExerciseSet(exerciseSet);
					
					tempExerciseId = exerciseId;
				}
				
				exerciseSet.addSet(set);
			}
			
			optDetails = Optional.ofNullable(details);
		}
		catch (SQLException | DBException e) {
			throw new DAOException("Faild to find workout details by id", e);
		}
		
		return optDetails;
	}

	@Override
	public List<WorkoutDetails> findAll() throws DAOException {
		
		List<WorkoutDetails> detailsList = new ArrayList<>();
		
		try (
				Connection connection = DBConnector.getConnection();
				Statement statement = connection.createStatement()
				) {
			
			ResultSet rs = statement.executeQuery(FIND_ALL_QUERY);
			
			WorkoutDetails details = null;
			ExerciseSet exerciseSet = null;
			Exercise exercise = null;
			int tempWorkoutId = -1;
			int tempExerciseId = -1;
			while (rs.next()) {
				
				int workoutId = rs.getInt("w_id");
				if (tempWorkoutId != workoutId)	{
					
					details = new WorkoutDetails();
					
					Workout workout = new Workout();
					workout.setId(
							rs.getInt("w_id")
							);
					workout.setDate(
							Date.valueOf(
									rs.getString("w_date")
									)
							);
					
					details.setWorkout(workout);
					
					detailsList.add(details);
					
					tempWorkoutId = workoutId;
				}
				
				int exerciseId = rs.getInt("e_id");
				if (tempExerciseId != exerciseId) {
					
					exercise = new Exercise();
					exercise.setId(rs.getInt("e_id"));
					exercise.setTitle(rs.getString("e_title"));
					
					exerciseSet = new ExerciseSet();
					exerciseSet.setExercise(exercise);
					
					details.addExerciseSet(exerciseSet);
					
					tempExerciseId = exerciseId;
				}
				
				double weight = rs.getDouble("weight");
				int reps = rs.getInt("reps");
				
				Set set = new Set();
				set.setWeight(weight);
				set.setReps(reps);
				
				exerciseSet.addSet(set);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to find all workout details", e);
		}
		
		return detailsList;
	}

	@Override
	public boolean update(WorkoutDetails model) throws DAOException {
		
		boolean isDeleted = false;
		boolean isSaved = false;
		boolean isUpdated = false;
		Optional<WorkoutDetails> optWorkoutDetails = Optional.empty(); 
		
		try {
			optWorkoutDetails = findById(model.getWorkout());
			isDeleted = deleteById(model.getWorkout());
			isSaved = save(model);
			isUpdated = isSaved;
			
		} catch (DAOException e) {
			
			if (isDeleted && optWorkoutDetails.isPresent()) {
				save(optWorkoutDetails.get());
			}
			
			throw new DAOException("Faild to update workout details", e);
		}
		
		return isUpdated;
	}

	@Override
	public boolean deleteById(Workout id) throws DAOException {
		
		boolean isDeleted = false;
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)
				) {
			
			statement.setInt(1, id.getId());
			int i = statement.executeUpdate();
			
			if (i > 0)
				isDeleted = true;
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to delete workout details", e);
		}
		
		return isDeleted;
	}

	@Override
	public Optional<WorkoutDetails> findByDate(Date date) throws DAOException {
		
		Optional<WorkoutDetails> optDetails = Optional.empty();
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_DATE_QUERY)
				) {
			
			statement.setString(1, date.toString());
			ResultSet rs = statement.executeQuery();
			
			WorkoutDetails details = null;
			ExerciseSet exerciseSet = null;
			Exercise exercise = null;
			int tempExerciseId = -1;
			boolean wereDetailsCreated = false;
			
			while (rs.next()) {
				if (wereDetailsCreated == false) {
					
					details = new WorkoutDetails();
					
					Workout workout = new Workout();
					workout.setId(rs.getInt("w_id"));
					workout.setDate(date);
					
					details.setWorkout(workout);
					
					wereDetailsCreated = true;
				}
				
				int exerciseId = rs.getInt("e_id");
				double weight = rs.getDouble("weight");
				int reps = rs.getInt("reps");
				
				Set set = new Set();
				set.setWeight(weight);
				set.setReps(reps);
				
				if (tempExerciseId != exerciseId) {
					
					exercise = new Exercise();
					exercise.setId(exerciseId);
					exercise.setTitle(rs.getString("e_title"));
					
					exerciseSet = new ExerciseSet();
					exerciseSet.setExercise(exercise);
					
					details.addExerciseSet(exerciseSet);
					
					tempExerciseId = exerciseId;
				}
				
				exerciseSet.addSet(set);
			}
			
			optDetails = Optional.ofNullable(details);
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to find workout details by date", e);
		}
		
		return optDetails;
	}

	@Override
	public List<WorkoutDetails> findByExerciseTitle(String title) throws DAOException{
		
		List<WorkoutDetails> detailsList = new ArrayList<>();;
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_EXERCISE_TITLE_QUERY)
				) {
			
			statement.setString(1, title);
			ResultSet rs = statement.executeQuery();
			
			WorkoutDetails details = null;
			ExerciseSet exerciseSet = null;
			Exercise exercise = null;
			int tempWorkoutId = -1;
			while (rs.next()) {
				
				int workoutId = rs.getInt("w_id");
				double weight = rs.getDouble("weight");
				int reps = rs.getInt("reps");
				
				Set set = new Set();
				set.setWeight(weight);
				set.setReps(reps);
				
				if (tempWorkoutId != workoutId) {
					
					details = new WorkoutDetails();
					
					Workout workout = new Workout();
					workout.setId(
							rs.getInt("w_id")
							);
					workout.setDate(
							Date.valueOf(
									rs.getString("w_date")
									)
							);
					
					exercise = new Exercise();
					exercise.setId(rs.getInt("e_id"));
					exercise.setTitle(title);
					
					exerciseSet = new ExerciseSet();
					exerciseSet.setExercise(exercise);
					
					details.setWorkout(workout);
					details.addExerciseSet(exerciseSet);
					
					detailsList.add(details);
					
					tempWorkoutId = workoutId;
				}
				
				exerciseSet.addSet(set);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to find workout details by exercise title", e);
		}
		
		return detailsList;
	}
}
