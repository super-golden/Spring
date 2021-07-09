package com.sr.tx.service.impl;

import com.sr.tx.dao.AccountInfoDao;
import com.sr.tx.dao.ProductInfoDao;
import com.sr.tx.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service("payServiceImpl")
public class PayServiceImpl  implements PayService {

	@Autowired
	private AccountInfoDao accountInfoDao;

	@Autowired
	private ProductInfoDao productInfoDao;

	@Override
	@Transactional
	public void pay(String accountId, double money) {
		double blance = accountInfoDao.qryBlanceByUserId(accountId);

		if(new BigDecimal(blance).compareTo(new BigDecimal(money)) <0){
			throw new RuntimeException("余额不足");
		}
		//更新余额

		int retVal = accountInfoDao.updateAccountBlance(accountId,money);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateProductStore(Integer productId) {

	}
}
