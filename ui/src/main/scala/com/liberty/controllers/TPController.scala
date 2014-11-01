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

class TPController extends jfxf.Initializable {

  @FXML private var stage1status: Label = null
  @FXML private var statusLabel: Label = null
  @FXML private var stage2status: Label = null
  @FXML private var stage3status: Label = null
  @FXML private var history_lv: ListView[String] = _
  @FXML private var zone3_status: Label = null
  @FXML private var zone3change: Label = null
  @FXML private var zone3current: Label = null
  @FXML private var zone3fan: ImageView = null
  @FXML private var zone3fire: ImageView = null
  @FXML private var zone3max: Label = null
  @FXML private var zone3min: Label = null
  @FXML private var zone4_status: Label = null
  @FXML private var zone4change: Label = null
  @FXML private var zone4current: Label = null
  @FXML private var zone4fan: ImageView = null
  @FXML private var zone4fire: ImageView = null
  @FXML private var zone4max: Label = null
  @FXML private var zone4min: Label = null
  @FXML private var zone5_status: Label = null
  @FXML private var zone5change: Label = null
  @FXML private var zone5current: Label = null
  @FXML private var zone5fan: ImageView = null
  @FXML private var zone5fire: ImageView = null
  @FXML private var zone5max: Label = null
  @FXML private var zone5min: Label = null
  @FXML private var zone6_status: Label = null
  @FXML private var zone6change: Label = null
  @FXML private var zone6current: Label = null
  @FXML private var zone6fan: ImageView = null
  @FXML private var zone6fire: ImageView = null
  @FXML private var zone6max: Label = null
  @FXML private var zone6min: Label = null
  @FXML private var startButton: Button = null
  @FXML private var chart: LineChart[Int, Int] = null
  private var chartCounter = 0
  val series  = new XYChart.Series()

  @FXML private var resources: ResourceBundle = null
  private var historyItems: ObservableList[String] = null

  @FXML
  private def onStartButton(event: jfxe.ActionEvent) {
    //changeStartButtonStatus(disable = true)
    startProcessing()
  }


  def startProcessing() {
    val zones: List[Zone] =
      Zone(1, PrepareCeramicMixture(1)) ::
        Zone(2, FormingBricks(2)) ::
        Zone(3, DryingBricks(TemperatureBounds(540, 720), 3)) ::
        Zone(4, DryingBricks(TemperatureBounds(720, 790), 4)) ::
        Zone(5, DryingBricks(TemperatureBounds(900, 920), 5)) ::
        Zone(6, DryingBricks(TemperatureBounds(540, 600), 6)) :: Nil

    TPModel.initialize(zones, this)
    new Thread(new Runnable {
      override def run(): Unit = {
        //  changeStatus(statusLabel, Zone.PROCESSING)
        zones.foreach(_.run())
        zones.last.operation.complete()
        changeStageStatus(3, Zone.COMPLETED)
        //   changeStatus(statusLabel, Zone.COMPLETED)
        // changeStartButtonStatus(disable = false)
      }
    }).start()
  }

  private def changeStartButtonStatus(disable: Boolean) {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        startButton.setDisable(disable)
      }
    })
  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    historyItems = FXCollections.observableArrayList()
    history_lv.setItems(historyItems)
  }

  def changeStageStatus(stageId: Int, status: Zone.Value) {
    stageId match {
      case 1 => changeStatus(stage1status, status)
      case 2 => changeStatus(stage2status, status)
      case 3 => changeStatus(stage3status, status)
    }
  }


  private def changeStatus(label: Label, status: Zone.Value) {
    import Zone._
    Platform.runLater(new Runnable() {

      override def run(): Unit = {
        status match {
          case NOT_STARTED => label.setText("NOT_STARTED")
            label.setTextFill(Color.RED)
          case PROCESSING => label.setText("PROCESSING")
            label.setTextFill(Color.DARKCYAN)
          case COMPLETED => label.setText("COMPLETED")
            label.setTextFill(Color.GREEN)
          case PREPARING => label.setText("PREPARING")
            label.setTextFill(Color.YELLOW)
          case _ =>
        }
      }
    })
  }

  def changeCurrentTemperature(zoneId: Int, t: Int, change: Int) {
    zoneId match {
      case 3 => changeCurrentTemperature(zone3current, zone3change, t, change, zone3fire, zone3fan)
      case 4 => changeCurrentTemperature(zone4current, zone4change, t, change, zone4fire, zone4fan)
      case 5 => changeCurrentTemperature(zone5current, zone5change, t, change, zone5fire, zone5fan)
      case 6 => changeCurrentTemperature(zone6current, zone6change, t, change, zone6fire, zone6fan)
      case _ =>
    }
  }

  private def changeCurrentTemperature(labelTemp: Label, labelChange: Label, t: Int, change: Int,
                                       fire: ImageView, fan: ImageView) {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        labelTemp.setText(t.toString)
        if (change == 0) {
          labelChange.setTextFill(Color.DARKBLUE)
          labelChange.setText(change.toString)
          fire.setVisible(false)
          fan.setVisible(false)
        }
        else if (change > 0) {
          labelChange.setTextFill(Color.DARKBLUE)
          labelChange.setText("+" + change.toString)
          fire.setVisible(true)
          fan.setVisible(false)
        }
        else {
          labelChange.setTextFill(Color.RED)
          labelChange.setText(change.toString)
          fire.setVisible(false)
          fan.setVisible(true)
        }
        addToChart(t)
      }
    })
  }

  def logHistory(msg: String) {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        historyItems.add(msg)
      }
    })
  }

  def setBounds(zoneId: Int, bounds: TemperatureBounds) {
    zoneId match {
      case 3 => setBounds(zone3min, zone3max, bounds)
      case 4 => setBounds(zone4min, zone4max, bounds)
      case 5 => setBounds(zone5min, zone5max, bounds)
      case 6 => setBounds(zone6min, zone6max, bounds)
      case _ =>
    }
  }

  private def setBounds(min: Label, max: Label, bounds: TemperatureBounds) {
    Platform.runLater(new Runnable() {
      override def run(): Unit = {
        min.setText(bounds.min.toString)
        max.setText(bounds.max.toString)
      }
    })
  }

  def changeZoneStatus(zoneId: Int, status: Zone.Value) {
    zoneId match {
      case 3 => changeStatus(zone3_status, status)
      case 4 => changeStatus(zone4_status, status)
      case 5 => changeStatus(zone5_status, status)
      case 6 => changeStatus(zone6_status, status)
      case _ =>
    }
  }

  def addToChart(temperature: Int) {
//    val ser = new XYChart.Series[Int,Int]()
//    ser.getData.add(new XYChart.Data(chartCounter, temperature))
//    chart.setTitle("Temperature")
//
//    //val series = XYChart.Series[Int, Int]("My portfolio", data)
//    chart.getData.add(ser)
  }
}