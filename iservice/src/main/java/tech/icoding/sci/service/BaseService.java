package tech.icoding.sci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
public abstract  class BaseService<M extends JpaRepository<T, ID>, T, ID>{

   @Autowired
   private M repository;
   public T findById(ID id){
       final Optional<T> optional = repository.findById(id);
       if(optional.isPresent()){
           return optional.get();
       }
       return null;
   }

    public Page<T> findAll(Pageable pageable){
       return repository.findAll(pageable);
    }

    public void deleteById(ID id){
       repository.deleteById(id);
    }

    public T save(T entity){
        return repository.save(entity);
    }

    public T update(T entity){
       return repository.save(entity);
    }
}
