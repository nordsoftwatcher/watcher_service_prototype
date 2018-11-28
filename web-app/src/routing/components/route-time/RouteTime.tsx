import React from 'react';

import classes from './RouteTime.module.css';
import colors from '../../../colors.module.css';

import { DateTime, Duration } from 'luxon';

import { IRoute, ICheckpoint } from '../../models/route';
import { IRouteInstance, ICompletedCheckpoint } from '../../models/route-instance';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { formatTime } from '../../utils/date';
import { cssPercent, percent } from './utils';
import { UUID } from '../../models/uuid';
import { getDeviations, Deviation } from '../../utils/deviations';

export interface RouteTimeProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
}

export class RouteTime extends React.Component<RouteTimeProps> {

  get planStart() {
    const { route } = this.props;
    const firstCheckpoint = route.checkpoints[0];
    if (!firstCheckpoint) {
      return undefined;
    }
    const { planArrival, planDeparute } = firstCheckpoint;
    return DateTime.fromJSDate(planArrival || planDeparute);
  }

  get planEnd() {
    const { route } = this.props;
    const lastCheckpoint = route.checkpoints[route.checkpoints.length - 1];
    if (!lastCheckpoint) {
      return undefined;
    }
    const { planDeparute, planArrival } = lastCheckpoint;
    return DateTime.fromJSDate(planDeparute || planArrival);
  }

  get factStart() {
    const { routeInstance } = this.props;
    const firstPos = routeInstance && routeInstance.track && routeInstance.track[0];
    if (firstPos) {
      return DateTime.fromJSDate(firstPos.attributes.timestamp);
    }
  }

  get factEnd() {
    const { routeInstance } = this.props;

    const lastPos = routeInstance && routeInstance.track && routeInstance.track[routeInstance.track.length - 1];
    if (lastPos) {
      return DateTime.fromJSDate(lastPos.attributes.timestamp);
    }
  }

  get start() {
    if (this.planStart && this.factStart) {
      return DateTime.min(this.planStart, this.factStart);
    } else {
      return this.planStart || this.factStart;
    }
  }

  get end() {
    if (this.planEnd && this.factEnd) {
      return DateTime.max(this.planEnd, this.factEnd);
    } else {
      return this.planEnd || this.factEnd;
    }
  }

  get duration() {
    if (this.end && this.start) {
      return this.end.diff(this.start);
    } else {
      return Duration.fromMillis(0);
    }
  }

  findPointInstance(pointId: UUID) {
    const { routeInstance } = this.props;

    if (!routeInstance) {
      return undefined;
    }

    return routeInstance.chekpoints.find(x => x.pointId === pointId);
  }

  renderPlannedRoute() {

    if (!this.planStart || !this.start) {
      return null;
    }

    if (!this.planEnd || !this.end) {
      return null;
    }

    const style: React.CSSProperties = {
      left: cssPercent(
        percent(
          this.planStart.diff(this.start).as('milliseconds'),
          this.duration.as('milliseconds'),
        ),
      ),
      right: cssPercent(
        percent(
          this.end.diff(this.planEnd).as('milliseconds'),
          this.duration.as('milliseconds'),
        ),
      ),
    };

    return (
      <div className={classes.plannedRoute} style={style} />
    );
  }

  renderTraveledRoute() {

    if (!this.factStart || !this.start) {
      return null;
    }

    if (!this.factEnd || !this.end) {
      return null;
    }

    const style: React.CSSProperties = {
      left: cssPercent(
        percent(
          this.factStart.diff(this.start).as('milliseconds'),
          this.duration.as('milliseconds'),
        ),
      ),
      right: cssPercent(
        percent(
          this.end.diff(this.factEnd).as('milliseconds'),
          this.duration.as('milliseconds'),
        ),
      ),
    };

    return (
      <div className={classes.traveledRoute} style={style} />
    );
  }

  renderCurrentPos() {
    const { routeInstance } = this.props;

    if (!routeInstance) {
      return null;
    }

    const currentPos = routeInstance.track[routeInstance.track.length - 1];

    if (!currentPos) {
      return null;
    }

    const currentPosTime = DateTime.fromJSDate(currentPos.attributes.timestamp);

    if (!this.start) {
      return null;
    }

    const style: React.CSSProperties = {
      left: `calc(
        ${cssPercent(
          percent(
            currentPosTime.diff(this.start).as('milliseconds'),
            this.duration.as('milliseconds'),
          ),
        )}
        - 12px)`,
    };

    return (
      <div className={classes.currentPos} style={style}>
        <CurrentPositionIcon />
      </div>
    );
  }

  renderCheckpoint(checkpoint: ICheckpoint) {

    const pointInstance = this.findPointInstance(checkpoint.id);

    const timestamp = pointInstance && pointInstance.arrival || checkpoint.planArrival;
    if (!timestamp) {
      return null;
    }

    if (!this.start) {
      return null;
    }

    const style: React.CSSProperties = {
      left: `calc(
        ${cssPercent(
          percent(
            DateTime.fromJSDate(timestamp).diff(this.start).as('milliseconds'),
            this.duration.as('milliseconds'),
          ),
        )}
        - 12px
      )`,
    };

    return (
      <div key={checkpoint.id} className={classes.checkpoint} style={style}>
        <RouteCheckpoint point={checkpoint} pointInstance={pointInstance} />
      </div>
    );
  }

  /* renderCompletedCheckpoints() {
    const { routeInstance } = this.props;

    if (!routeInstance) {
      return null;
    }

    return routeInstance.chekpoints.map(point => this.renderCompletedCheckpoint(point));
  }

  renderCompletedCheckpoint(checkpointInstance: ICompletedCheckpoint) {

    const { route } = this.props;

    const style: React.CSSProperties = {
      left: `calc(
        ${cssPercent(
          percent(
            DateTime.fromJSDate(checkpointInstance.arrival).diff(this.getStart()).as('milliseconds'),
            this.getDuration().as('milliseconds'),
          ),
        )}
        - 12px
      )`,
    };

    const checkpoint = route.checkpoints.find(x => x.id === checkpointInstance.pointId)!;

    return (
      <div key={checkpointInstance.pointId} className={classes.checkpoint} style={style}>
        <RouteCheckpoint point={checkpoint} pointInstance={checkpointInstance} />
      </div>
    );
  } */

  renderDeviations() {
    const deviations = getDeviations(this.props.routeInstance);
    return deviations.map((d, i) => this.renderDeviation(d, i));
  }

  renderDeviation(deviation: Deviation, key: any) {

    if (!this.start || !this.end) {
      return null;
    }

    const start = deviation[0];
    const end = deviation[deviation.length - 1];

    const style: React.CSSProperties = {
      left: cssPercent(
        percent(
          DateTime.fromJSDate(start.point.attributes.timestamp).diff(this.start).as('milliseconds'),
          this.duration.as('milliseconds'),
        ),
      ),
      right: cssPercent(
        percent(
          this.end.diff(DateTime.fromJSDate(end.point.attributes.timestamp)).as('milliseconds'),
          this.duration.as('milliseconds'),
        ),
      ),
    };

    return (
      <div key={key} className={classes.deviation} style={style} />
    );
  }

  render() {
    const { route } = this.props;

    return (
      <div className={classes.root}>
        {this.renderPlannedRoute()}
        {this.renderTraveledRoute()}
        {this.renderDeviations()}
        {route.checkpoints.map(point => this.renderCheckpoint(point))}
        {/* {this.renderCompletedCheckpoints()} */}
        {this.renderCurrentPos()}
      </div>
    );
  }

}

interface RouteCheckpointProps {
  point: ICheckpoint;
  pointInstance?: ICompletedCheckpoint;
}

const RouteCheckpoint: React.StatelessComponent<RouteCheckpointProps> =
  ({ point, pointInstance }) => {
    if (pointInstance) {
      return (
        <>
          <SuccessCheckpointIcon />
          <div className={classes.checkpointTime}>
            {formatTime(pointInstance.arrival)}
          </div>
        </>
      );
    } else {
      return (
        <>
          <FutureCheckpointIcon />
          <div className={classes.checkpointTime}>
            {formatTime(point.planArrival)}
          </div>
        </>
      );
    }
  };

const SuccessCheckpointIcon = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' />
    <FontAwesomeIcon className='fa-stack-1x' icon='check-circle' color={classes.successColor} />
  </div>
);

const FutureCheckpointIcon = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' color={colors.black} />
    <FontAwesomeIcon className='fa-stack-1x' icon='dot-circle' color={classes.mainColor} />
  </div>
);

const CurrentPositionIcon = () => (
  <div className='fa-stack'>
    <FontAwesomeIcon className='fa-stack-1x' icon='circle' />
    <FontAwesomeIcon className='fa-stack-1x' icon='user-circle' color={classes.successColor} />
  </div>
);
