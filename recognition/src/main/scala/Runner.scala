import java.net.Socket

import com.liberty.transmission.TransmissionManager

/**
 * User: dimitr
 * Date: 19.10.2014
 * Time: 13:01
 */
object Runner {
  def main(args: Array[String]) {
    TransmissionManager.startDataTransmission()

  }
}
