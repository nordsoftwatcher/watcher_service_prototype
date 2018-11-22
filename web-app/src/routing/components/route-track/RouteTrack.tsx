import React from 'react';
import styles from './RouteTrack.module.css';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRoute, ICheckpoint } from '../../models/route';
import { IRouteInstance, ICompletedCheckpoint } from '../../models/route-instance';
import { UUID } from '../../models/uuid';

export interface RouteTrackProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
}

export class RouteTrack extends React.Component<RouteTrackProps> {

  render() {
    return (
      <div className={styles.root}>
        <div className={styles.route} />
        <div className={styles.routeInstance} />
        <div className={styles.checkpoints}>
          {this.props.route.checkpoints.map(point => (
            <RouteCheckpoint key={point.id} point={point} pointInstance={this.findPointInstance(point.id)} />
          ))}
        </div>
        <div className={styles.currentPos}>
          <CurrentPosition />
        </div>
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
  ({ point, pointInstance }) => {
    if (pointInstance) {
      return (
        <div>
          <SuccessCheckpoint />
          <div className={styles.checkpointTime}>
            {pointInstance.arrival}
          </div>
        </div>
      );
    } else {
      return (
        <div>
          <FutureCheckpoint />
          <div className={styles.checkpointTime}>
            00:00
          </div>
        </div>
      );
    }
  };

const SuccessCheckpoint = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' />
    <FontAwesomeIcon className='fa-stack-1x' icon='check-circle' color={styles.successColor} />
  </div>
);

const FutureCheckpoint = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' color='#2A2B31' />
    <FontAwesomeIcon className='fa-stack-1x' icon='dot-circle' color={styles.mainColor} />
  </div>
);

const CurrentPosition = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' />
    <FontAwesomeIcon className='fa-stack-1x' icon='user-circle' color={styles.successColor} />
  </div>
);
