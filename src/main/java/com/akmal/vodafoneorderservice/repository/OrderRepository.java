package com.akmal.vodafoneorderservice.repository;

import com.akmal.vodafoneorderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
