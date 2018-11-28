import React from 'react';
import classes from './RouteMap.module.css';
import colors from '../../../colors.module.css';

// import 'leaflet/dist/leaflet.css'
import L from 'leaflet';
import { Map, Marker, TileLayer, Polyline, Tooltip } from 'react-leaflet';
import { IRoute, ICheckpoint } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { routePointIcon, passedPointIcon, currentPosIcon, sosIcon } from './icons';
import { UUID } from '../../models/uuid';
import { getDeviations } from '../../utils/deviations';
import { Coordinates } from '../../models/coordinates';
import { ITrackEvent } from '../../models/track-event';

interface RouteMapProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
  events: ITrackEvent[];
  onCheckpointClick?(checkpoint: ICheckpoint): void;
}

interface RouteMapState {
  zoom: number;
  center: Coordinates;
}

export class RouteMap extends React.Component<RouteMapProps, RouteMapState> {

  constructor(props: RouteMapProps) {
    super(props);

    const { route, routeInstance } = this.props;

    this.state = {
      center: routeInstance && routeInstance.currentPos || route.checkpoints[0].coords,
      zoom: 14,
    };
  }

  eventIcons: { [key: string]: L.Icon | L.DivIcon } = {
    SOS: sosIcon,
  };
  displayEvents = Object.keys(this.eventIcons);

  getPointById(pointId: UUID) {
    return this.props.route.checkpoints.find(x => x.id === pointId);
  }

  handleCheckpointClick = (checkpoint: ICheckpoint) => {
    const { onCheckpointClick } = this.props;
    if (onCheckpointClick) {
      onCheckpointClick(checkpoint);
    }
  }

  renderEvent = (event: ITrackEvent) => {
    if (!this.displayEvents.includes(event.type)) {
      return null;
    }

    return (
      <Marker key={event.id} position={event.coords} icon={this.eventIcons[event.type]}>
        <Tooltip direction='bottom' className={classes.tooltip} offset={[0, 15]}>
          {event.type} - {event.value}
        </Tooltip>
      </Marker>
    );
  }

  render() {
    const { route, routeInstance, events } = this.props;

    const deviations = getDeviations(routeInstance);

    return (
      <div className={classes.root}>
        <Map center={this.state.center} zoom={this.state.zoom}>
          <TileLayer
            attribution='&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
            maxZoom={19}
          />

          <Polyline positions={route.track} color={colors.blue} dashArray='1 7' />

          {route.checkpoints.map(point => (
            <Marker
              position={point.coords}
              key={point.id}
              icon={routePointIcon}
              onClick={() => this.handleCheckpointClick(point)}
            >
              <Tooltip direction='bottom' className={classes.tooltip}>
                {point.name}
              </Tooltip>
            </Marker>
          ))}

          {routeInstance && routeInstance.chekpoints.map(p => {
            const point = this.getPointById(p.pointId)!;
            return (
              <Marker
                position={point.coords}
                key={p.pointId}
                icon={passedPointIcon}
                onClick={() => this.handleCheckpointClick(point)}
              >
                <Tooltip direction='bottom' className={classes.tooltip}>
                  {point.name}
                </Tooltip>
              </Marker>
            );
          })}

          {routeInstance && <Polyline positions={routeInstance.track.map(x => x.coords)} color={colors.green} />}

          {deviations && deviations.length > 0 && (
            deviations.map((line, i) => (
              <Polyline color={colors.red} positions={line.map(x => x.point.coords)} key={i} />
            ))
          )}

          {routeInstance && routeInstance.currentPos &&
            <Marker position={routeInstance.currentPos} icon={currentPosIcon} zIndexOffset={1000} />
          }

          {events.map(this.renderEvent)}
        </Map>
      </div>
    );
  }
}
