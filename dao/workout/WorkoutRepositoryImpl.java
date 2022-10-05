package basis.dao.workout;

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
import basis.model.Workout;
import basis.util.DBConnector;

public class WorkoutRepositoryImpl implements WorkoutRepository {
	
	private final String SAVE_QUERY = "INSERT INTO workout "
			+ "(date) "
			+ "VALUES (?)";
	
	private final String FIND_BY_ID_QUERY = "SELECT date FROM workout "
			+ "WHERE id = ?";
	
	private final String FIND_BY_DATE_QUERY = "SELECT id FROM workout "
			+ "WHERE date = ?";
	
	private final String FIND_ALL_QUERY = "SELECT * FROM workout";
	
	private final String UPDATE_QUERY = "UPDATE workout "
			+ "SET date = ? "
			+ "WHERE id = ?";
	
	private final String DELETE_QUERY = "DELETE FROM workout "
			+ "WHERE id = ?";
	
	@Override
	public boolean save(Workout model) throws DAOException {
		
		boolean isSaved = false;
				
		Optional<Workout> optWorkout = findByDate(model.getDate());
		if (!optWorkout.isPresent()) {

			try (
					Connection connection = DBConnector.getConnection();
					PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)
					) {
				
				statement.setString(1, model.getDate().toString());
				int i = statement.executeUpdate();
				
				if (i > 0)
					isSaved = true;
				
			} catch (SQLException | DBException e) {
				throw new DAOException("Faild to save workout!", e);
			}
			
		}
		
		return isSaved;
	}

	@Override
	public Optional<Workout> findById(Integer id) throws DAOException {
		
		Optional<Workout> optWorkout = Optional.empty();
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)
				) {
			
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				
				String date = rs.getString("date");
				Workout workout = new Workout();
				workout.setId(id);
				workout.setDate(Date.valueOf(date));
				
				optWorkout = Optional.of(workout);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to find workout! by id", e);
		}
		
		return optWorkout;
	}
	
	@Override
	public Optional<Workout> findByDate(Date date) throws DAOException {
		
		Optional<Workout> optWorkout = Optional.empty();
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_DATE_QUERY)
				) {
			
			statement.setString(1, date.toString());
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				
				Integer id = rs.getInt("id");
				Workout workout = new Workout();
				workout.setId(id);
				workout.setDate(date);
				
				optWorkout = Optional.of(workout);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to find workout! by date", e);
		}
		
		return optWorkout;
	}

	@Override
	public List<Workout> findAll() throws DAOException {
		
		List<Workout> workouts = new ArrayList<>();
		
		try (
				Connection connection = DBConnector.getConnection();
				Statement statement = connection.createStatement()
				) {
			
			ResultSet rs = statement.executeQuery(FIND_ALL_QUERY);
			while (rs.next()) {
				
				Workout workout = new Workout();
				workout.setId(
						rs.getInt("id")
						);
				workout.setDate(
						Date.valueOf(
								rs.getString("date")
								)
						);
				
				workouts.add(workout);
			}
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to find all workouts", e);
		}
		
		return workouts;
	}

	@Override
	public boolean update(Workout model) throws DAOException {
		
		boolean isUpdated = false;
		
		try (
				Connection connection = DBConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
				) {
			
			statement.setString(1, model.getDate().toString());
			statement.setInt(2, model.getId());
			int i = statement.executeUpdate();
			
			if (i > 0)
				isUpdated = true;
			
		} catch (SQLException | DBException e) {
			throw new DAOException("Faild to update workout", e);
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
			throw new DAOException("Faild to delete workout", e);
		}
		
		return isDeleted;
	}
}
