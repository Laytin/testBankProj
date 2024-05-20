package com.testBankProj.laytin;

import com.testBankProj.laytin.dto.TransferRequest;
import com.testBankProj.laytin.dto.TransferResponce;
import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.repository.CustomerRepository;
import com.testBankProj.laytin.service.CustomerService;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class LaytinApplicationTests{
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerRepository customerRepository;
	@Test
	@Transactional
	public void testDefaultOne() {
		Customer owner = customerRepository.getById(16);
		float ownerBal = owner.getCurrentBalance();
		Customer destination = customerRepository.getById(17);
		float destBal = destination.getCurrentBalance();
		TransferRequest rq = new TransferRequest();
		rq.setOwnerId(owner.getId());
		rq.setDestUsername(destination.getUsername());
		rq.setSum(100);
		customerService.transferMoney(rq);
		assertTrue(ownerBal-100 == owner.getCurrentBalance(), "Owner bal:");
		assertTrue(destBal+100 == destination.getCurrentBalance(), "Destbal bal:");
	}
	@Test
	@Transactional
	public void destintaionNotFound() {
		Customer owner = customerRepository.getById(16);
		float ownerBal = owner.getCurrentBalance();
		TransferRequest rq = new TransferRequest();
		rq.setOwnerId(owner.getId());
		rq.setDestUsername("unexpecteduserasjhdjusioeghgyugds");
		rq.setSum(100);
		TransferResponce rs = customerService.transferMoney(rq);
		assertFalse(rs.isOk(), ""+rs.getMessage());
		assertTrue(ownerBal==owner.getCurrentBalance(),"owner bal=cur bal");
	}
	@Test
	public void ownerNotFound() {
		TransferRequest rq = new TransferRequest();
		rq.setOwnerId(99);
		rq.setDestUsername("unexpecteduserasjhdjusioeghgyugds");
		rq.setSum(100);
		TransferResponce rs = customerService.transferMoney(rq);
		assertFalse(rs.isOk(), ""+rs.getMessage());
	}
	@Test
	@Transactional
	public void sumIsToBig() {
		Customer owner = customerRepository.getById(16);
		float ownerBal = owner.getCurrentBalance();
		Customer destination = customerRepository.getById(17);
		float destBal = destination.getCurrentBalance();
		TransferRequest rq = new TransferRequest();
		rq.setOwnerId(owner.getId());
		rq.setDestUsername(destination.getUsername());
		rq.setSum(ownerBal+100);
		TransferResponce rs =customerService.transferMoney(rq);
		assertTrue(ownerBal==owner.getCurrentBalance(), "Owner bal:");
		assertTrue(destBal==destination.getCurrentBalance(), "Dest bal:");
		assertFalse(rs.isOk(), ""+rs.getMessage());
	}
	@Test
	public void sumIsNegative() {
		//nothing there, cuz @valid in controller and @min in  transactionRequest
	}
}
