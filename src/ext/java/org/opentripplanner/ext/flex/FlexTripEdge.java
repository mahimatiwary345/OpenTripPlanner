package org.opentripplanner.ext.flex;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.common.geometry.GeometryUtils;
import org.opentripplanner.ext.flex.distancecalculator.DistanceAndDuration;
import org.opentripplanner.ext.flex.distancecalculator.DistanceCalculator;
import org.opentripplanner.ext.flex.template.FlexAccessEgressTemplate;
import org.opentripplanner.ext.flex.trip.FlexTrip;
import org.opentripplanner.model.StopLocation;
import org.opentripplanner.model.Trip;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.core.StateEditor;
import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.graph.Edge;
import org.opentripplanner.routing.graph.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class FlexTripEdge extends Edge {

  private static final Logger LOG = LoggerFactory.getLogger(FlexTripEdge.class);

  private static final long serialVersionUID = 1L;

  public StopLocation s1;
  public StopLocation s2;
  private FlexTrip trip;
  FlexAccessEgressTemplate flexTemplate;
  public DistanceAndDuration distanceAndDuration;

  public FlexTripEdge(
      Vertex v1, Vertex v2, StopLocation s1, StopLocation s2, FlexTrip trip,
      FlexAccessEgressTemplate flexTemplate, DistanceCalculator calculator
  ) {
    super(new Vertex(null, null, 0.0, 0.0) {}, new Vertex(null, null, 0.0, 0.0) {});
    this.s1 = s1;
    this.s2 = s2;
    this.trip = trip;
    this.flexTemplate = flexTemplate;
    this.fromv = v1;
    this.tov = v2;
    // Why is this code so dirty? Because we don't want this edge to be added to the edge lists.
    this.distanceAndDuration = calculator.getDuration(fromv, tov, flexTemplate.fromStopIndex, flexTemplate.toStopIndex);
  }

  @Override
  public State traverse(State s0) {
    StateEditor editor = s0.edit(this);
    editor.setBackMode(TraverseMode.BUS);
    // TODO: decide good value
    editor.incrementWeight(10 * 60);
    int timeInSeconds = getTimeInSeconds();
    editor.incrementTimeInSeconds(timeInSeconds);
    editor.incrementWeight(timeInSeconds);
    editor.resetEnteredNoThroughTrafficArea();
    return editor.makeState();
  }

  public int getTimeInSeconds() {
    return distanceAndDuration.durationSeconds;
  }

  @Override
  public double getDistanceMeters() {
    return distanceAndDuration.distanceMeters;
  }

  @Override
  public LineString getGeometry() {
    return GeometryUtils.makeLineString(new Coordinate[] {
        fromv.getCoordinate(),
        tov.getCoordinate()
    });
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getName(Locale locale) {
    return this.getName();
  }

  @Override
  public Trip getTrip() {
    return trip.getTrip();
  }
}
