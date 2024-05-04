package com.example.backend.repository;

import com.example.backend.entity.Timesheet;
import com.example.backend.entity.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TimeSheetRepository extends JpaRepository<Timesheet, Long>  {

    boolean existsByUserAndFromDateLessThanEqualAndToDateGreaterThanEqual(User user, LocalDate fromDate,
                                                                          LocalDate toDate);

    boolean existsByUserAndFromDateAndToDate(User user, LocalDate fromDate, LocalDate toDate);

    List<Timesheet> findByUser(User user);

    List<Timesheet> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    List<Timesheet> findByUserOrderByCreatedAtAsc(User user, Pageable pageable);
    List<Timesheet> findAllByOrderByCreatedAtAsc();
    List<Timesheet> findAllByOrderByCreatedAtDesc();

    default List<Timesheet> findTop3ByUserOrderByCreatedAtDesc(User user) {
        return findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}
