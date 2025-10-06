package com.touin.thierry.sb.test.springboottester.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Transactional(transactionManager = "transactionManager")
public class PigBizServiceImpl implements PigBizService {

    @Autowired
    private MonAdaptateur monAdaptateur;


    @Override
    public void test() {
        monAdaptateur.doSomething();
    }

}
