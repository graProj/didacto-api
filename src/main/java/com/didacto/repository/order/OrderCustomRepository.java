package com.didacto.repository.order;

import com.didacto.domain.Member;
import com.didacto.domain.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderCustomRepository {
    Order autoOrder(Member member);
}
