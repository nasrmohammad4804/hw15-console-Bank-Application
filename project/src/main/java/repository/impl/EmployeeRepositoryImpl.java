package repository.impl;

import base.repository.impl.BaseRepositoryImpl;
import domain.Employee;
import repository.EmployeeRepository;

import javax.persistence.EntityManager;

public class EmployeeRepositoryImpl  extends BaseRepositoryImpl<Employee,Long>
implements EmployeeRepository {

    public EmployeeRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Employee> getEntityClass() {
        return Employee.class;
    }
}
