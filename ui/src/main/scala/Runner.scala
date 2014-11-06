import java.io.IOException
import javafx.{fxml => jfxf}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene

/** Example of using FXMLLoader from ScalaFX.
  */
object Runner extends JFXApp {

  val resource = getClass.getResource("fxml/ui.fxml")
  if (resource == null) {
    throw new IOException("Cannot load resource: GetProperty.fxml")
  }

  val root: jfxs.Parent = jfxf.FXMLLoader.load(resource)

  stage = new PrimaryStage() {
    title = "Simulation"
    scene = new Scene(root)
  }

}