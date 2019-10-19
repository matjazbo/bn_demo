package com.demo.movies.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.junit.AfterClass;
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

	private static final String SYNC_TEST_COUNTER_PREFIX = "counterTest-";
	private static final String ASYNC_TEST_COUNTER_PREFIX = "asyncCounterTest-";
	
	@Inject @Synchronous ImmediateCounterExecutor 	synchronousCounterExecutor;
	@Inject @Asynchronous AsyncCounterExecutor 		asynchronousCounterExecutor;
	@Inject CounterFactory counterFactory;
	@Inject CounterConfiguration counterConfiguration;
	
	@Inject MoviesConfiguration config;

	
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
        		.addPackages(true, Counter.class.getPackage())
        		.addPackages(true, Asynchronous.class.getPackage())
        		.addPackages(true, MoviesConfiguration.class.getPackage())
        		.addClass(MutableInt.class)
        		.addClass(Mutable.class)
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
    	logger.info("------------- TESTING COUNTER SYNCHRONOUSLY ---------------");
    	int counterId = new Random().nextInt(100);
    	
    	CounterService counterService = CounterService.initialize(synchronousCounterExecutor, counterFactory, counterConfiguration);
    	counterService.setExecutor(synchronousCounterExecutor);
    	Counter counter = counterService.increaseCounter(SYNC_TEST_COUNTER_PREFIX + counterId);
    	if (counter instanceof FileSystemCounter) {
    		String filePath = config.getCountersFilesystemPath();
			File f = new File(filePath, counter.getId());
			
			Long counterValueBefore = Long.valueOf(Files.readString(f.toPath()));
			logger.info("counterBefore={}", counterValueBefore);
    		counter.increaseCounter();
    		Long counterValueAfter = Long.valueOf(Files.readString(f.toPath()));
    		logger.info("counterAfter={}", counterValueAfter);
    		
    		assertTrue(counterValueAfter - counterValueBefore == 1L);	// counter's value must differ for exactly 1
    	}
    }
    
    @Test
    public void testCounterServiceAsynchronously() throws Exception {
    	logger.info("------------- TESTING COUNTER ASYNCHRONOUSLY ---------------");
    	CounterService counterService = CounterService.initialize(asynchronousCounterExecutor, counterFactory, counterConfiguration);
    	counterService.setExecutor(asynchronousCounterExecutor);
    	
    	int testSize = 100;	// was tested up to 3550 without problems
    	Map<Integer,Integer> counterCount = new HashMap<>(testSize);
    	logger.info("Running increaseCounter {} times in sequence, with no delay", testSize);
    	for (int i = 0; i < testSize; i++) {
			Integer counterId = new Random().nextInt(3);
			if (!counterCount.containsKey(counterId)) 
				counterCount.put(counterId, Integer.valueOf(1));		// initialize counter count
			else
				counterCount.put(counterId, counterCount.get(counterId)+1);	// increase counter count
			counterService.increaseCounter(ASYNC_TEST_COUNTER_PREFIX + counterId);
    	}
    	
    	int waitTime = testSize * 10;	// reduce the time per testSize for larger tests
    	logger.info("Waiting {} miliseconds for threads to finish writing to files", waitTime);
    	Thread.sleep(waitTime);	// wait until all the files are written
    	logger.info("Done waiting... let's check the results!");
    	
    	final MutableInt sumOfAllCounter = new MutableInt();
    	Files.list(new File(counterConfiguration.getCountersFilesystemPath()).toPath()).forEach(path -> {
    		try {
    			String fileName = path.toFile().getName();
    			if (!fileName.startsWith(ASYNC_TEST_COUNTER_PREFIX)) return;	// not the right file, skip
    			Integer counterId = Integer.parseInt(fileName.substring(ASYNC_TEST_COUNTER_PREFIX.length()));
				String counterValue = Files.readString(path);
				Integer valueInMap = counterCount.get(counterId);
				Integer valueInFile = Integer.valueOf(counterValue);
				assertEquals("valueInMap="+valueInMap+", valueInFile="+valueInFile, valueInMap, valueInFile);
				logger.debug("Counter {} in file {} passed, value={}", counterId, path, valueInFile);
				sumOfAllCounter.add(valueInFile);
			} catch (IOException e) {
				throw new RuntimeException("Could not read counter file " + path);
			}
    	});
    	
    	// all counters sum value must equal to the number of time the increaseCounter was called
    	assertEquals("sumOfAllCounter values from files="+sumOfAllCounter+", test was run " + testSize + " times.", 
    			sumOfAllCounter.intValue(), testSize);
    }
    
    @AfterClass
    public static void cleanup() {
    	Yaml yaml = new Yaml();
    	InputStream inputStream = CounterTest.class.getClassLoader().getResourceAsStream("config.yaml");
    	Map<String, Object> config = yaml.load(inputStream);
    	String path = String.valueOf(((Map)((Map)((Map)config.get("movies")).get("counters")).get("file-system")).get("path"));
    	File folder = new File(path);
		for (File f : folder.listFiles((dir, name) -> {
    		return name.startsWith(SYNC_TEST_COUNTER_PREFIX) || name.startsWith(ASYNC_TEST_COUNTER_PREFIX);
    	})) {
			f.deleteOnExit();
		}
		logger.info("Cleanup complete for {}", CounterTest.class.getName());
    }    
    
    	
}