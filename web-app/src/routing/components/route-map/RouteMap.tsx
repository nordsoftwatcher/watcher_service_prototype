import React from 'react';
import styles from './RouteMap.module.css';

// import 'leaflet/dist/leaflet.css'
import { Map, Marker, TileLayer, Polyline, Tooltip } from 'react-leaflet';
import { IRoute } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { routePointIcon, passedPointIcon, currentPosIcon } from './icons';
import { UUID } from '../../models/uuid';
import { getDeviations } from '../../utils/deviations';
import { Coordinates } from '../../models/coordinates';

interface RouteMapProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
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
      zoom: 15,
    };
  }

  getPointById(pointId: UUID) {
    return this.props.route.checkpoints.find(x => x.id === pointId);
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

          <Polyline positions={route.track} color='#01BEE9' dashArray='1 7' />

          {route.checkpoints.map(point => (
            <Marker position={point.coords} key={point.id} icon={routePointIcon}>
              <Tooltip direction='bottom' className={styles.tooltip}>
                {point.name}
              </Tooltip>
            </Marker>
          ))}

          {routeInstance && routeInstance.chekpoints.map(p => {
            const point = this.getPointById(p.pointId)!;
            return (
              <Marker position={point.coords} key={p.pointId} icon={passedPointIcon}>
                <Tooltip direction='bottom' className={styles.tooltip}>
                  {point.name}
                </Tooltip>
              </Marker>
            );
          })}

          {routeInstance && <Polyline positions={routeInstance.track.map(x => x.coords)} color='#06C575' />}

          {deviations && deviations.length > 0 && (
            deviations.map((line, i) => (
              <Polyline color='#F10F45' positions={line.map(x => x.point.coords)} key={i} />
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
