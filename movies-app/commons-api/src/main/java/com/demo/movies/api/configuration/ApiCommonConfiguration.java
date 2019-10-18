package com.demo.movies.api.configuration;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

/**
 * Class shared among all the configuration classes for the api layer.
 * 
 * @author Matjaz
 *
 */
public abstract class ApiCommonConfiguration implements CounterConfiguration {

	private ConfigurationUtil configUtil;
	
	/**
	 * Wrapper that exposes only the get function
	 * @author wigo
	 *
	 */
	protected class ConfigUtilWrapper {
		private ConfigurationUtil util;
		public ConfigUtilWrapper(ConfigurationUtil util) {
			this.util = util;
		}
		public Object get(String key) {
			return util.get(key);
		}
	}

    /**
     * Enables child classes to get config values explicitly
     * without annotations.
     * 
     * @return a wrapper to ConfigurationUtil that hides the not needed API of the underlying class
     */
	public ConfigUtilWrapper getConfig() {
    	if (configUtil==null) {
    		configUtil = ConfigurationUtil.getInstance();
    	}
		return new ConfigUtilWrapper(configUtil);
	}

}
