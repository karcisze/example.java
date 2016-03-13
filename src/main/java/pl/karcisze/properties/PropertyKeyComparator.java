package pl.karcisze.properties;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.logging.Logger;

/*
 * Comparator for Property keys. Keys are simple String and this comparator compares them in case insensitive way
 *
 * @author Krzysztof Arciszewski
 */
public class PropertyKeyComparator implements Comparator<String> {

	public final static Logger LOGGER = Logger.getLogger("AppProperties");

	/*
	 * Method to compare two Property keys in case insensitive way
	 *
	 * @param arg0 param to compare
	 * @param arg1 param to compare
	 * @return comparation result
	 */
	@Override
	public int compare(String arg0, String arg1) {
		if(arg0==null){
			throw new InvalidParameterException("arg0 paramter is null");
		}
		if(arg1==null){
			throw new InvalidParameterException("arg1 paramter is null");
		}
		String arg0Upper = arg0.toUpperCase();
		String arg1Upper = arg1.toUpperCase();
		return arg0Upper.compareTo(arg1Upper);
	}
}
