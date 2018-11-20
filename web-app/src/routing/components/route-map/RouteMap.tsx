import React from 'react';
import styles from './RouteMap.module.css';

// import 'leaflet/dist/leaflet.css'
import { Map, Marker, TileLayer, Polyline, Popup, Tooltip } from 'react-leaflet'
import { IRoute } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { routePointIcon, passedPointIcon, currentPosIcon } from './icons';

interface RouteMapProps {
  route: IRoute;
  routeInstance: IRouteInstance;
}

export class RouteMap extends React.Component<RouteMapProps> {

  getPointById(pointId: string) {
    return this.props.route.points.find(x => x.id === pointId);
  }

  render() {
    const { route, routeInstance } = this.props;

    return (
      <div className={styles.root}>
        <Map center={routeInstance.currentPos} zoom={13}>
          <TileLayer
            attribution='&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            maxZoom={19}
          />

          <Polyline positions={route.track} color='#01BEE9' dashArray='1 7'></Polyline>

          {
            route.points.map(point => (
              <Marker position={point.coord} key={point.id} icon={routePointIcon}>
                <Tooltip direction='bottom' className={styles.tooltip}>
                  {point.name}
                </Tooltip>
              </Marker>
            ))
          }
          {
            routeInstance.passedPoints.map(p => {
              const point = this.getPointById(p.pointId)!
              return (
                <Marker position={point.coord} key={p.pointId} icon={passedPointIcon}>
                  <Tooltip direction='bottom' className={styles.tooltip}>
                    {point.name}
                  </Tooltip>
                </Marker>
              )
            })
          }

          <Polyline positions={routeInstance.track} color='#06C575'></Polyline>
          
          <Marker position={routeInstance.currentPos} icon={currentPosIcon}></Marker>
        </Map>
      </div>
    )
  }
}