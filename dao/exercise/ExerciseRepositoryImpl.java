package basis.dao.exercise;

import java.util.Optional;

import basis.dao.exception.DAOException;
import basis.util.exception.DBException;
import basis.model.Exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import basis.util.DBConnector;

public class ExerciseRepositoryImpl implements ExerciseRepository {
	
	private final String SAVE_QUERY = "INSERT INTO exercise "
			+ "(title) "
			+ "VALUES (?)";
	
	private final String FIND_BY_ID_QUERY = "SELECT title FROM exercise "
			+ "WHERE id = ?";
	
	private final String FIND_BY_TITLE_QUERY = "SELECT id FROM exercise "
			+ "WHERE title = ?";
	
	private final String FIND_ALL_QUERY = "SELECT * FROM exercise";
	
	private final String UPDATE_QUERY = "UPDATE exercise "
			+ "SET title = ? "
			+ "WHERE id = ?";
	
	private final String DELETE_QUERY = "DELETE FROM exercise "
			+ "WHERE id = ?";
	
	@Override
	public boolean save(Exercise model) throws DAOException	{
		
		boolean isSaved = false;
				
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)
				) {
			
			statement.setString(1, model.getTitle());
			int i = statement.executeUpdate();
			
			if (i > 0)
				isSaved = true;
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Failed to save exercise", e);
		}
		
		return isSaved;
	}

	@Override
	public Optional<Exercise> findById(Integer id) throws DAOException {
		
		Optional<Exercise> optExercise = Optional.empty();
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)
				) {
			
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				
				String title = rs.getString("title");
				Exercise exercise = new Exercise();
				exercise.setId(id);
				exercise.setTitle(title);
				
				optExercise = Optional.of(exercise);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Failed to find exercise by id", e);
		}
		
		return optExercise;
	}
	
	@Override
	public Optional<Exercise> findByTitle(String title) throws DAOException {
		
		Optional<Exercise> optExercise = Optional.empty();
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_TITLE_QUERY)
				) {
			
			statement.setString(1, title);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				
				Integer id = rs.getInt("id");
				Exercise exercise = new Exercise();
				exercise.setId(id);
				exercise.setTitle(title);
				
				optExercise = Optional.of(exercise);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Failed to find exercise by title", e);
		}
		
		return optExercise;
	}

	@Override
	public List<Exercise> findAll() throws DAOException {
		
		List<Exercise> exercises = new ArrayList<>();
		
		try (
				Connection connection = DBConnector.getConnection();
				Statement statement = connection.createStatement()
				) {
			
			ResultSet rs = statement.executeQuery(FIND_ALL_QUERY);
			while (rs.next()) {
				
				Exercise exercise = new Exercise();
				exercise.setId(rs
						.getInt("id"));
				exercise.setTitle(rs
						.getString("title"));
				
				exercises.add(exercise);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Failed to find all exercises", e);
		}
		
		return exercises;
	}

	@Override
	public boolean update(Exercise model) throws DAOException {
		
		boolean isUpdated = false;
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
				) {
			
			statement.setString(1, model.getTitle());
			statement.setInt(2, model.getId());
			int i = statement.executeUpdate();
			
			if (i > 0)
				isUpdated = true;
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Failed to update exercise", e);
		}
		
		return isUpdated;
	}

	@Override
	public boolean deleteById(Integer id) throws DAOException {
		
		boolean isDeleted = false;
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
				) {
			
			statement.setInt(1, id);
			int i = statement.executeUpdate();
			
			if (i > 0)
				isDeleted = true;
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Failed to delete exercise", e);
		}
		
		return isDeleted;
	}
}
