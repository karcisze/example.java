package pl.karcisze.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example implementation of AppPropertiesManager interface
 *
 * Note: a default constructor is required
 *
 * @author Krzysztof Arciszewski
 */
public class TrialAppPropertiesManager implements AppPropertiesManager, AppPropertiesManagerExtensible {

	public final static Logger LOGGER = Logger.getLogger("AppProperties");

	private static final String URI_FILE = "file://";
	private static final String URI_HTTP = "http://";
	private static final String URI_CLASSPATH_RESOURCES = "classpath:resources/";

	protected PropertyCompletionHandler propertyCompletionHandler = PropertyCompletionHandler.getPropertyManager();

	/**
	 * Given a list of URIs and set of required keys, construct an AppProperties object.
	 *
	 * @param propUris an ordered list of properties files to load, keys in later URIs override old keys
	 * @return a fully constructed TrialProperties object
	 */
	@Override
	public synchronized AppProperties loadProps(List<String> propUris) {
		if(propUris==null){
			throw new InvalidParameterException("propUris paramter is null");
		}
		TrialAppProperties appProperties = new TrialAppProperties();
		PropertyFileParser parser = null;
		for (String propUri : propUris) {
			parser = selectParser(propUri);
			LOGGER.log(Level.INFO, "parser selected:["+parser.getClass().getName()+"] for URI:["+propUri+"]");

			if (propUri.startsWith(URI_FILE)) {
				LOGGER.log(Level.INFO, "URI type ["+URI_FILE+"] detected");
				loadPropsFromFile(appProperties, parser, propUri);
			}
			if (propUri.startsWith(URI_HTTP)) {
				LOGGER.log(Level.INFO, "URI type ["+URI_HTTP+"] detected");
				loadPropsFromHttp(appProperties, parser, propUri);
			}
			if (propUri.startsWith(URI_CLASSPATH_RESOURCES)) {
				LOGGER.log(Level.INFO, "URI type ["+URI_CLASSPATH_RESOURCES+"] detected");
				loadPropsFromClasspathResources(appProperties, parser, propUri);
			}
		}
		return appProperties;
	}

	/**
	 * Given instance of TrialAppProperties, adds into a Property of a given key and value.
	 * Actual value object put into appProperties container can be different than value passed as a parameter.
	 * Actual value and type of the object put depends on PropertyCompletionHandler, which is called to find
	 * assumed type of Property of the given key.
	 * Additionally, the method puts all the Properties that are in the same abstraction class.
	 * Abstraction classes of Properties are defined in PropertyCompletionHandler as well. So, adding one Property
	 * may lead to put more (still empty) Properties into the appProperties container.
	 * If Property of a given key already exists in the appProperties container, than the Property value is replaced
	 * by new value.
	 *
	 * @param appProperties container to put a new Property into
	 * @param key value of key that specifies Property to be put into appProperties container
	 * @param _value value of the Property to be put into appProperties container. It may happen
	 * @return a fully constructed TrialProperties object
	 */
	public synchronized void addProperty(TrialAppProperties appProperties, String key, Object _value) {
		if(appProperties==null){
			throw new InvalidParameterException("appProperties paramter is null");
		}
		if(key==null){
			throw new InvalidParameterException("key paramter is null");
		}
		if(_value==null){
			throw new InvalidParameterException("_value paramter is null");
		}
		Object value = null;
		String typeByKey = propertyCompletionHandler.getPropertyType(key);
		String valueType = _value.getClass().getName();
		if(typeByKey.equals(valueType)){
			LOGGER.log(Level.INFO, "Property value passed to add matches preconfigured type:["+typeByKey+"]");
			value = _value;
		}else{
			LOGGER.log(Level.INFO, "Type of property value passed:["+valueType+"] to add does NOT match preconfigured type:["+typeByKey+"]");
			value = propertyCompletionHandler.createCompletedPropertyValue(key, _value.toString());
		}
		Property property = new Property(key, value);

		if (!appProperties.properties.containsKey(key)) {
			TreeSet<String> closure = propertyCompletionHandler.getPropertyClosure(key);
			if (closure != null) {
				for (String kc : closure) {
					Property pc = Property.getEmptyProperty(kc);
					LOGGER.log(Level.INFO, "Closure Property with key:["+kc+"] being registered:["+pc.toString()+"]");
					appProperties.properties.put(kc, pc);
				}
			}
		}
		if (appProperties.properties.containsKey(key)) {
			LOGGER.log(Level.INFO, "Property with key:["+key+"] being replaced with:["+property.toString()+"]");
			appProperties.properties.replace(key, property);
		} else {
			LOGGER.log(Level.INFO, "New Property with key:["+key+"] being registered:["+property.toString()+"]");
			appProperties.properties.put(key, property);
		}
	}

	/*
	 * Loads Properties from classpath:resources/ located file
	 *
	 * @param appProperties container to put loaded Properties into
	 * @param parser parser to be used to load properties from given URI
	 * @param propUri URI location of resource file with Property definitions
	 */
	private void loadPropsFromClasspathResources(TrialAppProperties appProperties, PropertyFileParser parser,
			String propUri) {
			String path = propUri.substring(URI_CLASSPATH_RESOURCES.length());
			InputStream is = ClassLoader.getSystemResourceAsStream(path);
			if(is==null){
				//TODO
				throw new RuntimeException("resource can not be accessed");
			}
			parser.parseInto(is, appProperties);
			try {
				is.close();
			} catch (IOException e) {
				LOGGER.log(Level.ALL, "error closing input stream" + e);
			}
	}

	/*
	 * Loads Properties from http:// located file
	 *
	 * @param appProperties container to put loaded Properties into
	 * @param parser parser to be used to load properties from given URI
	 * @param propUri URI location of resource file with Property definitions
	 */
	private void loadPropsFromHttp(TrialAppProperties appProperties, PropertyFileParser parser, String propUri) {
		URL url = null;
		try {
			url = new URL(propUri);
		} catch (MalformedURLException e) {
			throw new AppPropertiesException("error creating URL from URI:["+propUri+"]",e);
		}
		InputStream is = null;
		try {
			is = url.openStream();
			parser.parseInto(is, appProperties);
		} catch (IOException e) {
			throw new AppPropertiesException("error opening stream for URL:["+url.toString()+"]",e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.log(Level.ALL, "error closing input stream" + e);
				}
			}
		}
	}

	/*
	 * Loads Properties from file:// located file
	 *
	 * @param appProperties container to put loaded Properties into
	 * @param parser parser to be used to load properties from given URI
	 * @param propUri URI location of resource file with Property definitions
	 */
	private void loadPropsFromFile(TrialAppProperties appProperties, PropertyFileParser parser, String propUri) {
		String path = propUri.substring(URI_FILE.length());
		File f = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			parser.parseInto(fis, appProperties);
		} catch (FileNotFoundException e) {
			throw new AppPropertiesException("file not found for URI:["+propUri+"]",e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOGGER.log(Level.ALL, "error closing file input stream" + e);
				}
			}
		}
	}

	/*
	 * @param propUri used to decide on what parser to used. It depends on file extension. Currently "*.json" and "*.properties" are supported.
	 * @return parser to be used to load Properties
	 */
	private PropertyFileParser selectParser(String propUri) {
		PropertyFileParser parser = null;
		if (propUri.endsWith(".json")) {
			parser = new PropertyFileParserJSON(this);
		}
		if (propUri.endsWith(".properties")) {
			parser = new PropertyFileParserProperty(this);
		}
		if (parser == null) {
			throw new AppPropertiesException("unsupported property file type of URI:["+propUri+"]");
		}
		return parser;
	}

	/**
	 * Prints out all TrialProperties to the given PrintStream in sorted,
	 * case insensitive, order by key name
	 *
	 * @param props properties to print
	 * @param sync a stream to write the properties to
	 */
	@Override
	public void printProperties(AppProperties props, PrintStream sync) {
		if(props==null){
			throw new InvalidParameterException("props paramter is null");
		}
		if(sync==null){
			throw new InvalidParameterException("sync paramter is null");
		}
		TrialAppProperties tprops = null;
		try {
			tprops = (TrialAppProperties) props;
		} catch (ClassCastException e) {
			throw new AppPropertiesException("props to be printed in not of TrialAppProperties type - which is invalid",e);
		}
		for (Entry<String, Property> e : tprops.properties.entrySet()) {
			Property p = e.getValue();
			sync.println(p);
		}
	}
}
