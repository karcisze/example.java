package pl.karcisze.properties;

import java.util.logging.Logger;

/*
 * Entity that represents entity handled by AppProperties class infrastructure.
 * It holds key and value of a property. Key is a String. Value is an Object of type that is assumed for property of the given Name.
 * Types of Objects are defined and handled by PropertyCompletionHandler class.
 *
 * @author Krzysztof Arciszewski
 */
public class Property {

	public final static Logger LOGGER = Logger.getLogger("AppProperties");

	public static final String PROPERTY_EMPTYVALUE = "";

	private final String key;
	private final Object value;

	private final PropertyCompletionHandler propertyCompletionHandler = PropertyCompletionHandler.getPropertyManager();

	/*
	 * @param key to generate empty Property with
	 * @return Property of a given key with empty value. Empty value means the Property is not defined
	 */
	public static final Property getEmptyProperty(String key){ return new Property(key, Property.PROPERTY_EMPTYVALUE); }

	/*
	 * @param key of the Property
	 * @param value of the Property
	 * @return new Property
	 */
	public Property(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	/*
	 * @return key of this Property
	 */
	public String getKey() {
		return key;
	}

	/*
	 * @return value of this Property
	 */
	public Object getValue() {
		return value;
	}

	/*
	 * @return String representation of instance of this class.
	 */
	@Override
	public String toString() {
		String r = key + ", " + propertyCompletionHandler.getPropertyType(key) + ", " + value;
		return r;
	}

	/*
	 * @return true if this Property is equal to the Property passed as a parameter. It compares both, key and value of Properties.
	 */
	@Override
	public boolean equals(Object p) {
		if(p==null){
			return false;
		}
		if(p instanceof Property) {
			return this.key.equals(((Property) p).key) && this.value.equals(((Property) p).value);
		}else{
			return false;
		}
	}
}