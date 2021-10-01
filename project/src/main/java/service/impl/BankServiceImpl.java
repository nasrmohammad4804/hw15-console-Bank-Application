package service.impl;

import domain.Bank;
import domain.embeddable.BankId;
import domain.enumeration.BankType;
import repository.impl.BankRepositoryImpl;
import service.BankService;

import java.util.ArrayList;
import java.util.List;

public class BankServiceImpl implements BankService {

   private BankRepositoryImpl repository;
    public BankServiceImpl(BankRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public List<Bank> findAllBank(String bankName) {
       return repository.findAllBank(bankName);
    }

    @Override
    public void addDefaultBank() {

        List<Bank> list=createDifferentBranchBankMelli();
        list.addAll(createDifferentBranchBankMellat());
        list.addAll(createDifferentBranchBankSaderat());
        list.addAll(createDifferentBranchBankTejarat());
        list.addAll(createDifferentBranchBankResalat());

        repository.getEntityManager().getTransaction().begin();

        for(int i=0; i<list.size(); i++){

            repository.getEntityManager().persist(list.get(i));

            if(i%50==0){
                repository.getEntityManager().flush();
                repository.getEntityManager().clear();
            }
        }
        repository.getEntityManager().getTransaction().commit();

        System.out.println("add default bank !!!\n");

    }

    @Override
    public Long countAll() {
        return  repository.countAll().longValue();
    }

    private List<Bank> createDifferentBranchBankSaderat(){

        List<Bank> list=new ArrayList<>();
        list.add(Bank.builder().id(new BankId(BankType.SADERAT.name(),1341L)).build());
        list.add(Bank.builder().id(new BankId(BankType.SADERAT.name(),1009L)).build());
        list.add(Bank.builder().id(new BankId(BankType.SADERAT.name(),765L)).build());
        list.add(Bank.builder().id(new BankId(BankType.SADERAT.name(),326L)).build());
        return list;

    }private List<Bank> createDifferentBranchBankMelli(){

        List<Bank> list=new ArrayList<>();
        list.add(Bank.builder().id(new BankId(BankType.MELLI.name(),1741L)).build());
        list.add(Bank.builder().id(new BankId(BankType.MELLI.name(),1103L)).build());
        list.add(Bank.builder().id(new BankId(BankType.MELLI.name(),854L)).build());
        list.add(Bank.builder().id(new BankId(BankType.MELLI.name(),928L)).build());
        return list;
    }private List<Bank> createDifferentBranchBankMellat(){

        List<Bank> list=new ArrayList<>();
        list.add(Bank.builder().id(new BankId(BankType.MELLAT.name(),3541L)).build());
        list.add(Bank.builder().id(new BankId(BankType.MELLAT.name(),451L)).build());
        list.add(Bank.builder().id(new BankId(BankType.MELLAT.name(),398L)).build());
        list.add(Bank.builder().id(new BankId(BankType.MELLAT.name(),167L)).build());
        return list;
    }private List<Bank> createDifferentBranchBankTejarat(){

        List<Bank> list=new ArrayList<>();
        list.add(Bank.builder().id(new BankId(BankType.TEJARAT.name(),761L)).build());
        list.add(Bank.builder().id(new BankId(BankType.TEJARAT.name(),1245L)).build());
        list.add(Bank.builder().id(new BankId(BankType.TEJARAT.name(),4521L)).build());
        list.add(Bank.builder().id(new BankId(BankType.TEJARAT.name(),1673L)).build());
        return list;
    }
    private List<Bank> createDifferentBranchBankResalat(){

        List<Bank> list=new ArrayList<>();
        list.add(Bank.builder().id(new BankId(BankType.RESALAT.name(),899L)).build());
        list.add(Bank.builder().id(new BankId(BankType.RESALAT.name(),1720L)).build());
        list.add(Bank.builder().id(new BankId(BankType.RESALAT.name(),2641L)).build());
        list.add(Bank.builder().id(new BankId(BankType.RESALAT.name(),552L)).build());
        return list;
    }

}
