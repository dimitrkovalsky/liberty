package com.liberty.helpers

import com.fasterxml.jackson.databind.{SerializationConfig, ObjectMapper, DeserializationConfig}


object JsonMapper {
  private val jsonMapper = new ObjectMapper()

  def getMapper: ObjectMapper = jsonMapper
}
