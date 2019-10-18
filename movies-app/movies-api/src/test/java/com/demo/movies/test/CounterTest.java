package com.demo.movies.test;


import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yaml.snakeyaml.Yaml;

import com.demo.movies.api.annotation.Asynchronous;
import com.demo.movies.api.annotation.Synchronous;
import com.demo.movies.api.configuration.CounterConfiguration;
import com.demo.movies.api.configuration.MoviesConfiguration;
import com.demo.movies.api.counter.Counter;
import com.demo.movies.api.counter.FileSystemCounter;
import com.demo.movies.api.counter.executor.AsyncCounterExecutor;
import com.demo.movies.api.counter.executor.ImmediateCounterExecutor;
import com.demo.movies.api.counter.factory.CounterFactory;
import com.demo.movies.api.counter.service.CounterService;

@RunWith(Arquillian.class)
public class CounterTest {
	
	private static final Logger logger = LogManager.getLogger(CounterTest.class);

	private static final String TEST_COUNTER_PREFIX = "counterTest-";
	
	@Inject @Synchronous ImmediateCounterExecutor 	synchronousCounterExecutor;
	@Inject @Asynchronous AsyncCounterExecutor 		asynchronousCounterExecutor;
	
	@Inject MoviesConfiguration config;


	
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
        		.addPackages(true, Counter.class.getPackage())
        		.addPackages(true, Asynchronous.class.getPackage())
        		.addClass(MoviesConfiguration.class)
                .addAsResource("config.yaml", "config.yaml") 
                .addAsResource("log4j2.xml", "log4j2.xml") 
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        logger.info(jar.toString(true));
        boolean exportToZip = false;
        if (exportToZip) {
	        ZipExporterImpl ze = new ZipExporterImpl(jar);
	        ze.exportTo(new File("c:/temp/test.jar"));
        }
        return jar;
    }
    
    @Test
    public void testCounterServiceSynchronously() throws Exception {
    	int counterId = new Random().nextInt(100);
    	
    	CounterService counterService = CounterService.getService();
    	Counter counter = counterService.increaseCounter(TEST_COUNTER_PREFIX + counterId);
    	System.out.println(counter);
    	if (counter instanceof FileSystemCounter) {
    		String filePath = config.getCountersFilesystemPath();
			File f = new File(filePath, counter.getId());
			
			Long counterValueBefore = Long.valueOf(Files.readString(f.toPath()));
			logger.info("counterBefore={}", counterValueBefore);
    		counter.increaseCounter();
    		Long counterValueAfter = Long.valueOf(Files.readString(f.toPath()));
    		logger.info("counterAfter={}", counterValueAfter);
    		
    		Assert.assertTrue(counterValueAfter - counterValueBefore == 1L);	// counter's value must differ for exactly 1
    	}
    }
    
    @AfterClass
    public static void cleanup() {
    	Yaml yaml = new Yaml();
    	InputStream inputStream = CounterTest.class.getClassLoader().getResourceAsStream("config.yaml");
    	Map<String, Object> config = yaml.load(inputStream);
    	String path = String.valueOf(((Map)((Map)((Map)config.get("movies")).get("counters")).get("file-system")).get("path"));
    	File folder = new File(path);
		for (File f : folder.listFiles((dir, name) -> {
    		return name.startsWith(TEST_COUNTER_PREFIX);
    	})) {
			f.deleteOnExit();
		}
		logger.info("Cleanup complete for {}", CounterTest.class.getName());
    }    
    
    	
}