package tech.icoding.sci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
public abstract class BaseService<M extends JpaRepository<T, ID>, T, ID> {

    @Autowired
    protected M repository;

    /**
     * 更具ID查找Entity
     * @param id
     * @return
     */
    public T findById(ID id) {
        return repository.findById(id).get();
    }

    /**
     * 返回的是一个引用，这里容易导致LazyInitializationException, 不推荐使用，建议使用findById
     * @Deprecated
     * @param id
     * @return
     */
    @Deprecated
    public T getOne(ID id){
        return repository.getOne(id);
    }
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public T update(T entity) {
        return repository.save(entity);
    }
}
