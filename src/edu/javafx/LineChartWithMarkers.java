package edu.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class LineChartWithMarkers extends LineChart<Number, Number> {
	
	private ObservableList<Marker> markers = FXCollections.observableArrayList();

    public LineChartWithMarkers() {
        super(new NumberAxis(), new NumberAxis());
        this.markers.addListener((InvalidationListener)observable -> layoutPlotChildren());
    }
    
	@Override
	public NumberAxis getXAxis() {
		return (NumberAxis) super.getXAxis();
	}

	@Override
	public NumberAxis getYAxis() {
		return (NumberAxis) super.getYAxis();
	}
	
	public final void addMarker(Data<Number, Number> point, String color) {
		addMarker(point, color, MarkerType.CROSS);
    }

    public final void addVerticalMarker(Data<Number, Number> point, String color) {
    	addMarker(point, color, MarkerType.VERTICAL);
    }
    
    public final void addHorizontalMarker(Data<Number, Number> point, String color) {
    	addMarker(point, color, MarkerType.HORIZONTAL);
    }
    
    private void addMarker(Data<Number, Number> point, String color, MarkerType markerType){
    	Marker marker = new Marker(point, color, markerType);
    	this.markers.add(marker);
    	getPlotChildren().addAll(marker.getMarkerComponents());
    	
    	layoutPlotChildren();
    }
    
    public final void removeVerticalMarker(Data<Number, Number> point) {
    	List<Marker> markers = findMarkers(point, MarkerType.VERTICAL); 
    	removeMarkers(markers);
    }
    
    public final void removeHorizontalMarker(Data<Number, Number> point) {
    	List<Marker> markers = findMarkers(point, MarkerType.HORIZONTAL); 
    	removeMarkers(markers);
    }
    
    public void clearMarkers() {
		removeMarkers(this.markers);
	}
    
    private List<Marker> findMarkers(Data<Number, Number> point, MarkerType markerType){
    	return markers.stream()
			.filter( m -> m.point.equals(point) && m.markerType.equals(markerType))
			.collect(Collectors.toList());
    }
    
	private void removeMarkers(List<Marker> removedMarkers) {
		List<Node> allMarkersComponents = removedMarkers.stream()
				.map(Marker::getMarkerComponents)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		Platform.runLater(() -> {
			getPlotChildren().removeAll(allMarkersComponents);
			this.markers.remove(removedMarkers);
		});
	}
    
    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        this.markers.forEach(Marker::layoutMarker);    
    }

	
	private class Marker{
		
		private final Data<Number, Number> point;
		private final Line verticalLine;
		private final Line horizontalLine;
		private final MarkerType markerType;
		private final Circle circle;
		private final Pane labelPane;
		
		private Marker(Data<Number, Number> point, String color, MarkerType markerType) {
			this.point = point;
			this.markerType = markerType;
			this.circle = createCircle(color);
			this.verticalLine = markerType.isVertical() ? createLine(color) : null;
			this.horizontalLine = markerType.isHorizontal() ? createLine(color) : null;
			
			String labelText = point.getYValue().toString() + ", " + point.getXValue().toString();
			this.labelPane = createLabelPane(labelText, color);
		}
		
		private Line createLine(String color){
			Line line = new Line();
			line.getStrokeDashArray().add(4d);
			line.setStyle("-fx-stroke: " + color + ";");
			return line;
		}
		
		private Circle createCircle(String color){
			Circle circle = new Circle(5);
			circle.setFill(Color.TRANSPARENT);
			circle.setStyle("-fx-stroke: " + color + ";");
			return circle;
		}
		
		private Pane createLabelPane(String labelText, String color){
			Label label = new Label(labelText);
			label.setMinHeight(20);
	        label.setMinWidth(120);
	        label.setStyle(" -fx-text-fill:" + color + ";");
	        
	        Pane pane = new Pane();
			pane.setStyle("-fx-background-color: transparent;");
			pane.getChildren().add(label);
			
			return pane;
		}
		
		public List<Node> getMarkerComponents(){
			List<Node> markerNodes = new ArrayList<>();
			
			if(this.horizontalLine != null)
				markerNodes.add(this.horizontalLine);
			if(this.verticalLine != null)
				markerNodes.add(this.verticalLine);
			markerNodes.add(circle);
			markerNodes.add(labelPane);
			
			return markerNodes;
		}
		
		public void layoutMarker(){
			Number xValue = this.point.getXValue();
			double xValuePosition = getXAxis().getDisplayPosition(xValue);
			double endX = getBoundsInLocal().getWidth();
			
			Number yValue = this.point.getYValue();
            double yValuePosition = getYAxis().getDisplayPosition(yValue);
            double endY = getBoundsInLocal().getHeight();
            
            if(this.verticalLine != null){
            	this.verticalLine.setStartX(xValuePosition);
            	this.verticalLine.setEndX(xValuePosition);
            	this.verticalLine.setStartY(0d);
            	this.verticalLine.setEndY(endY);
            	this.verticalLine.toFront();
            }
            
            if(this.horizontalLine != null){
            	this.horizontalLine.setStartX(0);
            	this.horizontalLine.setEndX(endX);
            	this.horizontalLine.setStartY(yValuePosition);
            	this.horizontalLine.setEndY(yValuePosition);
            	this.horizontalLine.toFront();
            }		
            
            this.circle.setCenterX(xValuePosition);
            this.circle.setCenterY(yValuePosition);
            this.circle.toFront();
            
            this.labelPane.setLayoutX(xValuePosition + 10);
            this.labelPane.setLayoutY(yValuePosition - 25);
            this.labelPane.toFront();
		}
		
	}
	
	private static enum MarkerType{
		VERTICAL, HORIZONTAL, CROSS;
		
		public boolean isVertical(){
			return this == VERTICAL || this == CROSS;
		}
		
		public boolean isHorizontal(){
			return this == HORIZONTAL || this == CROSS;
		}
	}

}
