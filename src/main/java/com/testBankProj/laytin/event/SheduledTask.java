package com.testBankProj.laytin.event;

import com.testBankProj.laytin.dao.CustomerDAO;
import com.testBankProj.laytin.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class SheduledTask {
    private final CustomerDAO customerDAO;
    private final Logger log= LogManager.getLogger(SheduledTask.class);
    @Autowired
    public SheduledTask(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void chargeInterest(){
        customerDAO.updateBalance();
        log.info("Scheduled task executed");
    }
}
