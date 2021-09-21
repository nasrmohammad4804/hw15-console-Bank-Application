package service.impl;

import base.service.impl.BaseServiceImpl;
import domain.Employee;
import repository.impl.EmployeeRepositoryImpl;
import service.EmployeeService;

public class EmployeeServiceImpl  extends BaseServiceImpl<Employee,Long, EmployeeRepositoryImpl>
 implements EmployeeService {

    protected EmployeeServiceImpl(EmployeeRepositoryImpl repository) {
        super(repository);
    }
}
