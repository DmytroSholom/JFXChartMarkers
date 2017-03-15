package edu.javafx;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Scene scene = new Scene(createLineChart(),400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private LineChart createLineChart(){
        //creating the chart
        LineChartWithMarkers lineChart = 
                new LineChartWithMarkers();
        lineChart.getXAxis().setLabel("Number of Month");
        
                
        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        Series<Number, Number> series = new Series<>();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new Data<Number, Number>(1, 23));
        series.getData().add(new Data<Number, Number>(2, 14));
        series.getData().add(new Data<Number, Number>(3, 15));
        series.getData().add(new Data<Number, Number>(4, 24));
        series.getData().add(new Data<Number, Number>(5, 34));
        series.getData().add(new Data<Number, Number>(6, 36));
        series.getData().add(new Data<Number, Number>(7, 22));
        series.getData().add(new Data<Number, Number>(8, 45));
        series.getData().add(new Data<Number, Number>(9, 43));
        series.getData().add(new Data<Number, Number>(10, 17));
        series.getData().add(new Data<Number, Number>(11, 29));
        series.getData().add(new Data<Number, Number>(12, 25));
        
        lineChart.addVerticalMarker(new Data<Number, Number>(7, 22), "blue");
        lineChart.addHorizontalMarker(new Data<Number, Number>(7, 22), "blue");
        lineChart.addVerticalMarker(new Data<Number, Number>(8, 22), "blue");
        lineChart.addHorizontalMarker(new Data<Number, Number>(10, 17), "blue");
        
        lineChart.getData().add(series);
        
        return lineChart;
	}
}
