package com.symphodia.eng;

import com.symphodia.eng.core.graph.OrderStatus;
import com.symphodia.eng.integrations.logeventstorage.LogEventsStorage;
import com.symphodia.eng.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ManagementApi {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    LogEventsStorage logEventsStorage;

    @RequestMapping("/getOrderState")
    public OrderStatus getOrderState(final @RequestParam(value="id") String id) {
        return orderRepository.findOne(id).getStatus();
    }

    @RequestMapping(value = "/cleanLogs", method = RequestMethod.GET)
    public String cleanLogs() {
        logEventsStorage.cleanLogs();
        return "OK";
    }

}
