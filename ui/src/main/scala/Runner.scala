import java.io.IOException
import javafx.animation.{KeyFrame, Timeline}
import javafx.fxml.FXMLLoader
import javafx.scene.{Scene, Parent}
import javafx.stage.StageStyle
import javafx.util.Duration
import javafx.{fxml => jfxf}
import javafx.{scene => jfxs}
import com.liberty.controllers.Controller
import com.liberty.handlers.TreeLoadingEventHandler

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.stage.Stage

/** Example of using FXMLLoader from ScalaFX.
  */
object Runner extends JFXApp {

//  val resource = getClass.getResource("fxml/uipattern.fxml")
//  if (resource == null) {
//    throw new IOException("Cannot load resource: GetProperty.fxml")
//  }
//
//  val root: jfxs.Parent = jfxf.FXMLLoader.load(resource)
//
//  stage = new PrimaryStage() {
//    title = "Simulation"
//    scene = new Scene(root)
//  }
////  private var loader: FXMLLoader = null
//
//  def start(primaryStage: Stage) {
//    val loader: FXMLLoader = new FXMLLoader(getClass.getResource("resources/fxml/uipattern.fxml"))
//    this.loader = loader
//    val root: Parent = loader.load.asInstanceOf[Parent]
//    val controller: Controller = loader.getController[Controller]
//    controller.scanProjectDirectory(primaryStage)
////    val timeline: Timeline = new Timeline(new KeyFrame(Duration.seconds(10), new TreeLoadingEventHandler(controller, primaryStage)))
////    timeline.setCycleCount(Timeline.INDEFINITE)
////    timeline.play
//    primaryStage.setScene(new Scene(root, 1280, 760))
//    primaryStage.initStyle(StageStyle.TRANSPARENT)
//    primaryStage.show
//  }
//
//  def getLoader: FXMLLoader = {
//    return loader
//  }
}