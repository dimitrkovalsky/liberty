package com.liberty.controllers

import com.liberty.common.{GrammarGroups, GrammarIds}
import com.liberty.common.emulation.{VoiceEmulator, _}
import com.liberty.transmission.TransmissionManager
import com.liberty.types.primitives.{IntegerType, StringType, FloatType}

/**
 * Created by Maxxis on 11/22/2014.
 */
class EmulateController {
  def emulate(): Unit = {
    VoiceEmulator.emulate {
            GrammarIds.CREATE_PROJECT *> 0 //0 - timeout, the same as *>()
            GrammarIds.NAME_OF_PROJECT :> "simple" // set name
      GrammarGroups.COMPONENT_CREATION <* 0 // Change active grammar to COMPONENT_CREATION
      GrammarIds.CREATE_CLASS *> 0
      GrammarIds.NAME_OF_CLASS :> "device"
      // emulate
      GrammarGroups.CLASS_FIELD_CREATION :>> "description" x StringType
      GrammarGroups.CLASS_FIELD_CREATION :>> "price" x FloatType
      GrammarGroups.CLASS_FIELD_CREATION :>> "id" x IntegerType
      GrammarIds.COMPLETE_CLASS_EDITING *> 3000
      GrammarIds.CREATE_DAO *> 0
      GrammarIds.CREATE_BEAN *> 0
      GrammarIds.CREATE_REST *> 1000
      GrammarIds.RUN_MONGO *> 100
      GrammarIds.BUILD_AND_DEPLOY *> 100
      GrammarIds.OPEN_BROWSER *> 100
    }
    TransmissionManager.startDataTransmission(emulate = true)
  }

}
