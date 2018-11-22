import React from 'react';
import styles from './RouteMap.module.css';

// import 'leaflet/dist/leaflet.css'
import { Map, Marker, TileLayer, Polyline, Tooltip } from 'react-leaflet';
import { IRoute } from '../../models/route';
import { IRouteInstance, ITrackCoordiantes } from '../../models/route-instance';
import { routePointIcon, passedPointIcon, currentPosIcon } from './icons';
import { UUID } from '../../models/uuid';

interface RouteMapProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
}

export class RouteMap extends React.Component<RouteMapProps> {

  deviationThreshold = 0.00025;

  getPointById(pointId: UUID) {
    return this.props.route.checkpoints.find(x => x.id === pointId);
  }

  getDeviations() {
    const { routeInstance } = this.props;
    if (!routeInstance) {
      return [];
    }

    type Deviation = Array<{ point: ITrackCoordiantes; index: number }>;

    const deviatePoints = routeInstance.track
      .map((point, index) => ({ point, index }))
      .filter(x => x.point.attributes.distanceFromRoute > this.deviationThreshold);

    const deviateLines = deviatePoints.reduce((acc, currPoint) => {
      const lastLine = acc[acc.length - 1];
      if (!lastLine) {
        acc.push([currPoint]);
      } else {
        const lastPoint = lastLine[lastLine.length - 1];
        if (Math.abs(lastPoint.index - currPoint.index) === 1) {
          lastLine.push(currPoint);
        } else {
          acc.push([currPoint]);
        }
      }
      return acc;
    }, [] as Deviation[]);

    deviateLines.forEach(line => {
      const first = line[0];
      const beforeFirst = routeInstance.track[first.index - 1];
      if (beforeFirst) {
        line.unshift({ point: beforeFirst, index: first.index - 1 });
      }
      const last = line[line.length - 1];
      const afterLast = routeInstance.track[last.index + 1];
      if (afterLast) {
        line.push({ point: afterLast, index: last.index + 1 });
      }
    });

    return deviateLines;
  }

  render() {
    const { route, routeInstance } = this.props;
    const centerMap = routeInstance && routeInstance.currentPos || route.checkpoints[0].coords;

    const deviations = this.getDeviations();

    return (
      <div className={styles.root}>
        <Map center={centerMap} zoom={15}>
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
            <Marker position={routeInstance.currentPos} icon={currentPosIcon} />
          }
        </Map>
      </div>
    );
  }
}
