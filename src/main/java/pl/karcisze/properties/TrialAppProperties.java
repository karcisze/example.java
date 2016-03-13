package pl.karcisze.properties;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Example implementation of AppProperties interface
 *
 * note: a default constructor is required.
 *
 * @author Krzysztof Arciszewski
 */
public class TrialAppProperties implements AppProperties {

	public final static Logger LOGGER = Logger.getLogger("AppProperties");

	protected TreeMap<String, Property> properties = new TreeMap<String, Property>(new PropertyKeyComparator());

	/*
     * @return a list of properties that are unset either because they are missing or because they have the wrong type
	 */
	@Override
	public List<String> getMissingProperties() {
		List<String> keyList = new ArrayList<String>();
		Set<String> keys = properties.keySet();
		for(String key: keys){
			Property val = (Property)properties.get(key);
			if(val.equals(Property.getEmptyProperty((key)))){
				keyList.add(key);
			}
		}
		return keyList;
	}

	/*
     * @return a list of all known keys
	 */
	@Override
	public List<String> getKnownProperties() {
		List<String> keyList = new ArrayList<String>();
		Set<String> keySet = properties.keySet();
		keyList.addAll(keySet);
		return keyList;
	}

	/*
	 * @return true if all registered Properties have non empty value - false otherwise
	 */
	@Override
	public boolean isValid() {
		List<String> keysMissing = getMissingProperties();
		if(keysMissing.isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	/*
	 * Method used to reset all loaded properties to null / unloaded
	 */
	@Override
	public void clear() {
		Set<String> keys = properties.keySet();
		for(String key: keys){
			Property emptyProperty = Property.getEmptyProperty(key);
			properties.replace(key, emptyProperty);
		}
	}

	/*
	 * Method to access Properties by their key.
	 *
	 * @param key of Property to be returned.
	 * @return Property of a given key, or null if such Property has not been registered.
	 */
	@Override
	public Object get(String key) {
		if(key == null) {
			throw new InvalidParameterException("key paramter is null");
		}
		Object value = properties.get(key); // TODO get should return value not entry
		return value;
	}

	/*
	 * @return String representation of instance of this class.
	 */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		Set<String> keys = properties.keySet();
		for(String k: keys){
			Property p = properties.get(k);
			sb.append(p);
			sb.append(";");
		}
		return sb.toString();		
	}
}
