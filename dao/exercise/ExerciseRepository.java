package basis.dao.exercise;

import java.util.Optional;
import basis.dao.DAO;
import basis.dao.exception.DAOException;
import basis.model.Exercise;

public interface ExerciseRepository extends DAO<Exercise, Integer> {
	Optional<Exercise> findByTitle(String title) throws DAOException;
}
