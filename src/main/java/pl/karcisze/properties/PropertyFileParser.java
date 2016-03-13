package pl.karcisze.properties;

import java.io.InputStream;

/*
 * Base class for implementation of parsers for specific property file formats.
 * It assumes that subclasses implement parseInto method, that is supposed to parse file provided by InputStream instance,
 * and to put extracted properties into a given instance of TrialAppProperties class.
 *
 * @author Krzysztof Arciszewski
 */
public abstract class PropertyFileParser {

	protected AppPropertiesManagerExtensible appPropertiesManagerExtensible = null;

	/*
	 * Constructor to create Parser with a given AppPropertiesManagerExtensible instance.
	 * The instance is used to call back addProperty function.
	 *
	 * @param appPropertiesManagerExtensible
	 */
	public PropertyFileParser(AppPropertiesManagerExtensible appPropertiesManagerExtensible){
		this.appPropertiesManagerExtensible = appPropertiesManagerExtensible;
	}

	/*
	 * Parses given InputStream in order to define Properties and to put them into TrialAppProperties instance
	 *
	 * @param fi InputStream of data to be parsed
	 * @param appProperties container to put Properties into
	 */
	abstract protected void parseInto(InputStream fi, TrialAppProperties appProperties);
}
