package basis.dao.workoutdetails;

import java.sql.Date;

import java.util.List;
import java.util.Optional;

import basis.dao.DAO;
import basis.dao.exception.DAOException;
import basis.model.Workout;
import basis.model.WorkoutDetails;


public interface WorkoutDetailsRepository extends DAO<WorkoutDetails, Workout> {
	Optional<WorkoutDetails> findByDate(Date date) throws DAOException;
	List<WorkoutDetails> findByExerciseTitle(String title) throws DAOException ;
}
