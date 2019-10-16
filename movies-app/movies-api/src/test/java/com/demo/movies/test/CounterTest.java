package com.demo.movies.test;


import java.util.Random;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.demo.movies.api.annotation.Asynchronous;
import com.demo.movies.api.counter.Counter;
import com.demo.movies.api.counter.service.CounterService;

@RunWith(Arquillian.class)
public class CounterTest {
	
	@Inject CounterService counterService;
	
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
        		.addPackages(true, Counter.class.getPackage())
        		.addPackages(true, Asynchronous.class.getPackage())
                .addAsResource("config.yaml", "config.yaml") 
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void testCounterService() {
    	int counterId = new Random().nextInt(100);
    	Counter counter = counterService.increaseCounter("test-" + counterId);
    	/*
    	if (counter instanceof FileSystemCounter) {
    		
    	}
    	*/
    }
    	
}