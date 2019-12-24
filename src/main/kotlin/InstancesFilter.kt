/**
 * Represents filter which uses [Map] to filter instances with tags which tagname-tagvalue pair is in the [attrToValueMap].
 */
class InstancesFilter(private val attrToValueMap: Map<String, String>) : Map<String, String> by attrToValueMap