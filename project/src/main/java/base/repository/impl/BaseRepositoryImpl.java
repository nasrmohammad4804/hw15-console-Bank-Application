package base.repository.impl;

import base.domain.BaseEntity;
import base.repository.BaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<E extends BaseEntity<ID>,ID extends Serializable>
 implements BaseRepository<E,ID> {

    protected EntityManager entityManager;
    protected CriteriaBuilder criteriaBuilder;

    public BaseRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        criteriaBuilder=entityManager.getCriteriaBuilder();
    }

    public  abstract Class<E> getEntityClass();
    @Override
    public E save(E element) {
        entityManager.persist(element);
        return element;
    }

    @Override
    public void delete(E element) {
        element.setIsDeleted(true);
    }

    @Override
    public E update(E element) {
       return entityManager.merge(element);
    }

    @Override
    public Optional<E> findById(ID id) {
        Optional<E> optional=Optional.empty();
        try {
            optional=Optional.of(entityManager.find(getEntityClass(),id));
            return optional;

        }catch (Exception e){
            e.printStackTrace();
            return optional;
        }
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteriaQuery=criteriaBuilder.createQuery(getEntityClass());
        Root<E> root=criteriaQuery.from(getEntityClass());
        criteriaQuery.select(root);
       return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
