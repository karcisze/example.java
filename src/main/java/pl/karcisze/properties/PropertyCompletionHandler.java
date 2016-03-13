package pl.karcisze.properties;

import com.amazonaws.regions.Regions;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class is designed as a single point to hold and manage Property types and Property abstraction classes.
 * Each Property (of a given name) has a type. Constant values of propertyKeysInitial and propertyTypesInitial
 * specify types for Properties of given names, and provide information on what are abstraction classes of each of
 * Properties.
 * Abstraction class is a set of Properties that must be registered in AppProperties instance if any of Properties
 * in that abstraction class is registered in the instance.
 *
 * One is allowed to extend this class with another Properties (keys and types). One is also welcome to extend
 * createCompletedPropertyValue method which is dedicated to new create values of Properties, if propertyTypesInitial variable
 * provides a specific type for a Property (which is different from java.lang.String). Type registered in propertyTypesInitial
 * for a given Property key must be the same as type of value returned by createCompletedPropertyValue method
 * for the Property key.
 *
 * This is supporting class for TrialAppPropertiesManager.
 *
 * @author Krzysztof Arciszewski
 */
public class PropertyCompletionHandler {

	// TODO NEW PROPERTY TYPES must be defined in their abstraction classes here ...
	protected static final String[][] propertyKeysInitial = { { "JDBC_DRIVER", "JDBC_URL", "JDBC_USERNAME", "JDBC_PASSWORD" },
			{ "hibernate.generate_statistics", "hibernate.show_sql" },
			{ "aws_access_key", "aws_secret_key", "aws_account_id", "aws_region_id" }, { "jpa.showSql" },
			{ "JPA_SHOWSQL" }, { "auth.endpoint.uri" }, { "job.timeout" }, { "score.factor" },
			{ "sns.broadcast.topic_name" } };
	protected static final String[][] propertyTypesInitial = {
			{ "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String" },
			{ "java.lang.Boolean", "java.lang.Boolean" },
			{ "java.lang.String", "java.lang.String", "java.lang.Long", "com.amazonaws.regions.Regions" },
			{ "java.lang.Boolean" }, { "java.lang.Boolean" }, { "java.lang.String" }, { "java.lang.Long" },
			{ "java.lang.Double" }, { "java.lang.String" } };


	public static final String PROPERTY_TYPE_DEFAULT = "java.lang.String";

	private static PropertyCompletionHandler propertyCompletionHandlerSingleton = null;

	/*
	 * @return returns (and creates eventually) singleton of PropertyCompletionHandler
	 */
	public static PropertyCompletionHandler getPropertyManager() {
		if (propertyCompletionHandlerSingleton == null) {
			propertyCompletionHandlerSingleton = new PropertyCompletionHandler(propertyKeysInitial, propertyTypesInitial);
		}
		return propertyCompletionHandlerSingleton;
	}

	public final static Logger LOGGER = Logger.getLogger("AppProperties");


	private Map<String, TreeSet<String>> propertyClosures = new HashMap<String, TreeSet<String>>();
	private Map<String, String> propertyTypes = new HashMap<String, String>();

	/*
     * Constructor to create PropertyCompletionHandler with a given initial configuration.
     * It is supposed to be used for unit testing purposes only.
     * In production environment, please use getPropertyManager static method, that returns a singleton
     *
	 * @param _propertyKeys abstraction classes of Properties (given by Property keys)
	 * @param _propertyTypes types of Properties
	 */
	protected PropertyCompletionHandler(String[][] _propertyKeys, String[][] _propertyTypes) {
		if(_propertyKeys==null){
			throw new InvalidParameterException("_propertyKeys is null");
		}
		if(_propertyTypes==null){
			throw new InvalidParameterException("_propertyTypes is null");
		}
		if(_propertyKeys.length!=_propertyTypes.length){
			throw new InvalidParameterException("incompatible lenghts of _propertyKeys and _propertyKeys");
		}
		for(int i=0; i<_propertyKeys.length; i++){
			if(_propertyKeys[i]==null){
				throw new InvalidParameterException("null in _propertyKeys in position "+i);
			}
			if(_propertyTypes[i]==null){
				throw new InvalidParameterException("null in _propertyTypes in position "+i);
			}
			if(_propertyKeys[i].length != _propertyTypes[i].length){
				throw new InvalidParameterException("different lenght between _propertyKeys and _propertyTypes in position "+i);
			}
		}

		for (int i = 0; i < _propertyKeys.length; i++) {
			TreeSet<String> closure = new TreeSet<String>(new PropertyKeyComparator());
			int closureArraySize = _propertyKeys[i].length;
			for (int j = 0; j < closureArraySize; j++) {
				String key = _propertyKeys[i][j];
				closure.add(key);
			}
			for (int j = 0; j < closureArraySize; j++) {
				String key = _propertyKeys[i][j];
				propertyClosures.put(key, closure);
				propertyTypes.put(key, _propertyTypes[i][j]);
			}
		}
	}

	/*
	 * @param propertyKey used to select all Property keys in its abstraction class. Abstraction classes are predefined in propertyKeysInitial constant
	 * @return set of all keys in abstraction class of Property of a given key
	 */
	protected TreeSet<String> getPropertyClosure(String propertyKey) {
		return propertyClosures.get(propertyKey);
	}

	/*
	 * @param propertyKey value used to select Property type, that is predefined in propertyTypesInitial constant
	 * @return String representation of Property type of a given key
	 */
	protected String getPropertyType(String propertyKey) {
		String type = propertyTypes.get(propertyKey);
		if (type == null) {
			type = PROPERTY_TYPE_DEFAULT;
		}
		return type;
	}

	/*
	 * @return String representation of instance of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Set<String> keys = propertyClosures.keySet();
		for (String k : keys) {
			Set<String> closure = propertyClosures.get(k);
			sb.append(k + ":");
			for (String kc : closure) {
				String tc = getPropertyType(kc);
				sb.append("(" + kc + "," + tc + ")");
			}
		}
		return sb.toString();
	}

	/*
	 * Method dedicated to create Property values, which type of conform to the Property registration in
	 * propertyKeysInitial and propertyTypesInitial (constant values defined in this class).
	 * If parameter value does not match requirements to create given object type, then empty value is returned -
	 * this means type mismatch.
	 *
	 * @param key key of a Property value to be created. It is used to define type of the value object.
	 * 		  Types are defined in propertyTypesInitial constant in this class.
	 * @param value value of a Property. This value is directly returned by this method, or this value is used
	 *        to create actual value of Property of type of the key.
	 * @return value of the Property, directly returned, or newly created
	 */
	protected Object createCompletedPropertyValue(String key, String value) {
		String type = getPropertyType(key);
		if ("java.lang.String".equals(type)) {
			LOGGER.log(Level.INFO, "String value is passed by:["+value+"]");
			return value;
		}
		if ("java.lang.Boolean".equals(type)) {
			if ("true".equals(value)) {
				LOGGER.log(Level.INFO, "Booleqn(true) to be created:["+value+"]");
				return new Boolean(true);
			} else if ("false".equals(value)) {
				LOGGER.log(Level.INFO, "Booleqn(false) to be created:["+value+"]");
				return new Boolean(false);
			} else {
				LOGGER.log(Level.WARNING, "Boolean type creation: passed value is not 'true' nor 'false' - returning empty property value");
				return Property.PROPERTY_EMPTYVALUE;
			}
		}
		if ("java.lang.Integer".equals(type)) {
			try {
				LOGGER.log(Level.INFO, "Integer to be created:["+value+"]");
				return new Integer(value);
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Integer type creation: passed value does not represent numerical value:["+value+"] - returning empty property value");
				return Property.PROPERTY_EMPTYVALUE;
			}
		}
		if ("java.lang.Long".equals(type)) {
			try {
				LOGGER.log(Level.INFO, "Long to be created:["+value+"]");
				return new Long(value);
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Long type creation: passed value does not represent numerical value:["+value+"] - returning empty property value");
				return Property.PROPERTY_EMPTYVALUE;
			}
		}
		if ("java.lang.Double".equals(type)) {
			try {
				LOGGER.log(Level.INFO, "Double to be created:["+value+"]");
				return new Double(value);
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Double type creation: passed value does not represent numerical value:["+value+"] - returning empty property value");
				return Property.PROPERTY_EMPTYVALUE;
			}
		}
		if ("com.amazonaws.regions.Regions".equals(type)) {
			try {
				LOGGER.log(Level.INFO, "AWS Regions to be created:["+value+"]");
				return Regions.fromName(value);
			} catch (java.lang.IllegalArgumentException e){
				LOGGER.log(Level.WARNING, "AWS Regions type creation: passed value does not AWS Region identifier:["+value+"] - returning empty property value");
				return Property.PROPERTY_EMPTYVALUE;
			}
		}

		// TODO NEW PROPERTY TYPES must be handled here
		// ...

		throw new AppPropertiesException("property type:["+type+"] not supported");
	}
}
