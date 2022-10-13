package com.transaction.transac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.transaction.transac.models.TransactionModel;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Integer>{
    TransactionModel findByUserId(String userId);
}
