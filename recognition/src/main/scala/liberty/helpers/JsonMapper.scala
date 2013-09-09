package liberty.helpers

import org.codehaus.jackson.map.{DeserializationConfig, SerializationConfig, ObjectMapper}


object JsonMapper {
    private val jsonMapper = new ObjectMapper()
    jsonMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false)
    jsonMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    def getMapper: ObjectMapper = jsonMapper
}
