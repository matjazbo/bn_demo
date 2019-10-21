package com.demo.movies.dependency;

import java.util.Arrays;
import java.util.List;

import com.kumuluz.ee.testing.arquillian.spi.MavenDependencyAppender;

public class RequiredDependencyAppender implements MavenDependencyAppender {

	@Override
	public List<String> addLibraries() {
		return Arrays.asList(new String[] { "com.kumuluz.ee.logs:kumuluzee-logs-log4j2:1.3.1" });
	}

}
