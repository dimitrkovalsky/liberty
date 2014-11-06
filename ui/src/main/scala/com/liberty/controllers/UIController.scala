package com.liberty.controllers

import java.net.URL
import java.util
import javafx.scene.{control => jfxsc}
import javafx.{event => jfxe}
import javafx.{fxml => jfxf}
import javafx.fxml.FXML
import com.liberty.common.{TemperatureBounds, Zone}
import javafx.scene.control.{Label, ListView, Button}
import com.liberty.processing.{TPModel, DryingBricks, FormingBricks, PrepareCeramicMixture}
import scalafx.scene.paint.Color
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.Button
import javafx.scene.control.Label
import java.util.ResourceBundle
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
/**
 * User: Dmytro_Kovalskyi
 * Date: 06.11.2014
 * Time: 17:02
 */
class UIController extends jfxf.Initializable {

  @FXML  private var dataLabel: Label = null

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    dataLabel.setText("simple text")
  }
}
