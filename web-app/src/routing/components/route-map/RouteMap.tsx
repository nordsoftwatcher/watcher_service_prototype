import React from 'react';
import styles from './RouteMap.module.css';
import colors from '../../../colors.module.css';

// import 'leaflet/dist/leaflet.css'
import { Map, Marker, TileLayer, Polyline, Tooltip } from 'react-leaflet';
import { IRoute, ICheckpoint } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { routePointIcon, passedPointIcon, currentPosIcon } from './icons';
import { UUID } from '../../models/uuid';
import { getDeviations } from '../../utils/deviations';
import { Coordinates } from '../../models/coordinates';

interface RouteMapProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
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

  getPointById(pointId: UUID) {
    return this.props.route.checkpoints.find(x => x.id === pointId);
  }

  handleCheckpointClick = (checkpoint: ICheckpoint) => {
    const { onCheckpointClick } = this.props;
    if (onCheckpointClick) {
      onCheckpointClick(checkpoint);
    }
  }

  render() {
    const { route, routeInstance } = this.props;

    const deviations = getDeviations(routeInstance);

    return (
      <div className={styles.root}>
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
              <Tooltip direction='bottom' className={styles.tooltip}>
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
                <Tooltip direction='bottom' className={styles.tooltip}>
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
        </Map>
      </div>
    );
  }
}
