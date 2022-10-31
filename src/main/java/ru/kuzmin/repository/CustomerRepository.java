package ru.kuzmin.repository;

import ru.kuzmin.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query("from CustomerEntity e " +
            "where " +
            "   concat(e.name, ' ', e.gender, ' ', e.birthday) like concat('%', :name, '%')")
    List<CustomerEntity> findByName(@Param("name") String name);
}
