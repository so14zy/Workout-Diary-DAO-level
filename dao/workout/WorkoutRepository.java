package basis.dao.workout;

import java.sql.Date;

import java.util.Optional;

import basis.dao.DAO;
import basis.dao.exception.DAOException;
import basis.model.Workout;

public interface WorkoutRepository extends DAO<Workout, Integer> {
	Optional<Workout> findByDate(Date date) throws DAOException;
}
