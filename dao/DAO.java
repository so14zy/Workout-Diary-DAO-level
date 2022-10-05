package basis.dao;

import java.util.List;
import java.util.Optional;

import basis.dao.exception.DAOException;

public interface DAO<Model, Id> {
	boolean save(Model model) throws DAOException;
	Optional<Model> findById(Id id) throws DAOException;
	List<Model> findAll() throws DAOException;
	boolean update(Model model) throws DAOException;
	boolean deleteById(Id id) throws DAOException;
}
