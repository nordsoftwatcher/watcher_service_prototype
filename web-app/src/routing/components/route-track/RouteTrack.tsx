import React from 'react';
import styles from './RouteTrack.module.css';
import colors from '../../../colors.module.css';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import turf from '@turf/turf';

import { IRoute, ICheckpoint } from '../../models/route';
import { IRouteInstance, ICompletedCheckpoint } from '../../models/route-instance';
import { UUID } from '../../models/uuid';
import { Coordinates } from '../../models/coordinates';
import { getDeviations, Deviation } from '../../utils/deviations';
import { formatTime } from '../../utils/date';

export interface RouteTrackProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
}

export class RouteTrack extends React.Component<RouteTrackProps> {

  private getRouteTrackAsLineString() {
    return turf.lineString(this.props.route.track.map(x => [x.lat, x.lng]));
  }

  private getRouteDistance() {

    // @ts-ignore
    const routeLength: number = turf.lineDistance(
      this.getRouteTrackAsLineString(),
      'meters',
    );

    return routeLength;
  }

  private getDistanceToPoint(point: Coordinates) {

    const { route } = this.props;

    const start = route.track[0];

    // @ts-ignore
    const line = turf.lineSlice(
      turf.point([start.lat, start.lng]),
      turf.point([point.lat, point.lng]),
      this.getRouteTrackAsLineString(),
    );

    // @ts-ignore
    return turf.lineDistance(
      line,
      'meters',
    );
  }

  private getDistanceRatio(point: Coordinates) {
    return this.getDistanceToPoint(point) / this.getRouteDistance() * 100;
  }

  private renderCurrentPos() {
    const { routeInstance } = this.props;

    if (!routeInstance || !routeInstance.currentPos) {
      return null;
    }

    const posRatio = this.getDistanceRatio(routeInstance.currentPos);
    const style: React.CSSProperties = {
      left: `calc(${posRatio}% - 13px)`,
    };

    return (
      <div className={styles.currentPos} style={style}>
        <CurrentPositionIcon />
      </div>
    );
  }

  private renderCheckpoint(checkpoint: ICheckpoint) {

    const posRatio = this.getDistanceRatio(checkpoint.coords);
    const style: React.CSSProperties = {
      left: `calc(${posRatio}% - 12px)`,
    };

    return (
      <div style={style} key={checkpoint.id} className={styles.checkpoint}>
        <RouteCheckpoint point={checkpoint} pointInstance={this.findPointInstance(checkpoint.id)} />
      </div>
    );
  }

  private renderTraveledRoute() {
    const { routeInstance } = this.props;

    if (!routeInstance || !routeInstance.currentPos) {
      return null;
    }

    const widthRatio = this.getDistanceRatio(routeInstance.currentPos);
    const style: React.CSSProperties = {
      width: `${widthRatio}%`,
    };

    return (
      <div className={styles.routeInstance} style={style} />
    );
  }

  private renderDeviations() {
    const deviations = getDeviations(this.props.routeInstance);

    return deviations.map((d, i) => this.renderDeviation(d, i));
  }

  private renderDeviation(deviation: Deviation, key: any) {

    const startPosRatio = this.getDistanceRatio(deviation[0].point.coords);
    const endPosRatio = this.getDistanceRatio(deviation[deviation.length - 1].point.coords);
    const widthRatio = endPosRatio - startPosRatio;

    const style: React.CSSProperties = {
      left: `${startPosRatio}%`,
      width: `${widthRatio}%`,
    };

    return (
      <div key={key} className={styles.deviation} style={style} />
    );
  }

  render() {
    const { route } = this.props;

    return (
      <div className={styles.root}>
        <div className={styles.route} />
        {this.renderTraveledRoute()}
        {this.renderDeviations()}
        {route.checkpoints.map(point => this.renderCheckpoint(point))}
        {this.renderCurrentPos()}
      </div>
    );
  }

  private findPointInstance = (pointId: UUID) => {
    if (this.props.routeInstance) {
      return this.props.routeInstance.chekpoints.find(x => x.pointId === pointId);
    }
  }
}

interface RouteCheckpointProps {
  point: ICheckpoint;
  pointInstance: ICompletedCheckpoint | undefined;
}

const RouteCheckpoint: React.StatelessComponent<RouteCheckpointProps> =
  ({ pointInstance }) => {
    if (pointInstance) {
      return (
        <>
          <SuccessCheckpointIcon />
          <div className={styles.checkpointTime}>
            {formatTime(pointInstance.arrival)}
          </div>
        </>
      );
    } else {
      return (
        <>
          <FutureCheckpointIcon />
          <div className={styles.checkpointTime}>
            00:00
          </div>
        </>
      );
    }
  };

const SuccessCheckpointIcon = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' />
    <FontAwesomeIcon className='fa-stack-1x' icon='check-circle' color={styles.successColor} />
  </div>
);

const FutureCheckpointIcon = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' color={colors.black} />
    <FontAwesomeIcon className='fa-stack-1x' icon='dot-circle' color={styles.mainColor} />
  </div>
);

const CurrentPositionIcon = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' />
    <FontAwesomeIcon className='fa-stack-1x' icon='user-circle' color={styles.successColor} />
  </div>
);
