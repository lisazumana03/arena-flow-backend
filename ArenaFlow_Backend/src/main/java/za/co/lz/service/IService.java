package za.co.lz.service;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {
    T create (T t);
    List<T> findAll();
    Optional<T> findById(ID id);
    T update (T t, ID id);
    void delete (ID id);
}
