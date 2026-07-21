package za.co.lz.service;

import java.util.List;

public interface IService<T, ID> {
    T create (T t);
    List<T> findAll();
    T update (T t, ID id);
    void delete (ID id);
}
