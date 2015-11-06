package com.itranswarp.recurring.api;

import javax.inject.Named;

@Named("test")
public class TestBean {

    public TestBean() {
        System.out.println("Init test bean...... ok!");
    }

}
