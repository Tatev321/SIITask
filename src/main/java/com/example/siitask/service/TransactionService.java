package com.example.siitask.service;


import com.example.siitask.model.Transaction;
import com.example.siitask.repo.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        transactionRepository.delete(transaction);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction dbTransaction = transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        dbTransaction.setTimestamp(transaction.getTimestamp());
        dbTransaction.setActor(transaction.getActor());
        dbTransaction.setType(transaction.getType());
        dbTransaction.setTransactionData(transaction.getTransactionData());
        return transactionRepository.save(dbTransaction);
    }

    public List<Transaction> searchTransactions(String type, String actor, Timestamp start, Timestamp end) {
        Specification<Transaction> spec = withFilters(type, actor, start, end);
        return transactionRepository.findAll(spec);
    }

    private Specification<Transaction> withFilters(String type, String actor, Timestamp start, Timestamp end) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (type != null && !type.isBlank()) {
                    predicates.add(cb.equal(root.get("type"), type));
                }

                if (actor != null && !actor.isBlank()) {
                    predicates.add(cb.equal(root.get("actor"), actor));
                }

                if (start != null && end != null) {
                    predicates.add(cb.between(root.get("timestamp"), start, end));
                } else if (start != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), start));
                } else if (end != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), end));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
