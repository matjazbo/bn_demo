package com.demo.movies.api.counter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileSystemCounter extends AbstractCounter {

	private static final Logger logger = LogManager.getLogger(FileSystemCounter.class);

	private File file;

	protected FileSystemCounter() {}

	/**
	 * Note: Is public, because factory class is not in the same package.
	 */
	public static FileSystemCounter newInstance() {
		return new FileSystemCounter();
	}

	@Override
	public synchronized void increaseCounter() throws IOException {
		if (!file.exists()) {
			logger.debug("File {} for counter {} does not exist. Creating new file.", file.getAbsolutePath(), getId());
			file.createNewFile();
		}
		String currentCount = Files.readString(file.toPath());
		long count = 1;
		if (currentCount != null && !currentCount.isBlank()) {
			count = Long.valueOf(currentCount);
			logger.trace("Found counter {} value {} in file {}", getId(), count, file.getAbsolutePath());
			count++;
		}
		Files.writeString(file.toPath(), String.valueOf(count));
		logger.debug("Counter {} increased to {} in file {}", getId(), count, file.getAbsolutePath());
		//System.out.println(String.format("Counter %s increased to %d in file %s", getId(), count, file.getAbsolutePath()));
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}