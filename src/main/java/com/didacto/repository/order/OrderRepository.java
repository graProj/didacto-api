package com.didacto.repository.order;
import com.didacto.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository{


}

