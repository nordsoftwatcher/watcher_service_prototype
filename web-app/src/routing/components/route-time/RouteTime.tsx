import React from 'react';

import classes from './RouteTime.module.css';
import colors from '../../../colors.module.css';

import { DateTime } from 'luxon';

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

  getPlanStart() {
    const { route } = this.props;
    return DateTime.fromJSDate(route.checkpoints[0].planArrival);
  }

  getPlanEnd() {
    const { route } = this.props;
    return DateTime.fromJSDate(
      route.checkpoints[route.checkpoints.length - 1].planDeparute ||
      route.checkpoints[route.checkpoints.length - 1].planArrival,
    );
  }

  getFactStart() {
    const { routeInstance } = this.props;
    const firstPos = routeInstance && routeInstance.track && routeInstance.track[0];
    if (firstPos) {
      return DateTime.fromJSDate(firstPos.attributes.timestamp);
    }
  }

  getFactEnd() {
    const { routeInstance } = this.props;

    const lastPos = routeInstance && routeInstance.track && routeInstance.track[routeInstance.track.length - 1];
    if (lastPos) {
      return DateTime.fromJSDate(lastPos.attributes.timestamp);
    }
  }

  getStart() {
    const factStart = this.getFactStart();
    if (factStart) {
      return DateTime.min(this.getPlanStart(), factStart);
    } else {
      return this.getPlanStart();
    }
  }

  getEnd() {
    const factEnd = this.getFactEnd();
    if (factEnd) {
      return DateTime.max(this.getPlanEnd(), factEnd);
    } else {
      return this.getPlanEnd();
    }
  }

  getDuration() {
    return this.getEnd().diff(this.getStart());
  }

  findPointInstance(pointId: UUID) {
    const { routeInstance } = this.props;

    if (!routeInstance) {
      return undefined;
    }

    return routeInstance.chekpoints.find(x => x.pointId === pointId);
  }

  renderPlannedRoute() {
    const planStart = this.getPlanStart();
    const planEnd = this.getPlanEnd();

    const style: React.CSSProperties = {
      left: cssPercent(
        percent(
          planStart.diff(this.getStart()).as('milliseconds'),
          this.getDuration().as('milliseconds'),
        ),
      ),
      right: cssPercent(
        percent(
          this.getEnd().diff(planEnd).as('milliseconds'),
          this.getDuration().as('milliseconds'),
        ),
      ),
    };

    return (
      <div className={classes.plannedRoute} style={style} />
    );
  }

  renderTraveledRoute() {
    const factStart = this.getFactStart();
    const factEnd = this.getFactEnd();

    if (!factStart || !factEnd) {
      return null;
    }

    const style: React.CSSProperties = {
      left: cssPercent(
        percent(
          factStart.diff(this.getStart()).as('milliseconds'),
          this.getDuration().as('milliseconds'),
        ),
      ),
      right: cssPercent(
        percent(
          this.getEnd().diff(factEnd).as('milliseconds'),
          this.getDuration().as('milliseconds'),
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

    if (!routeInstance.track[routeInstance.track.length - 1]) {
      return null;
    }

    const currentPos = DateTime.fromJSDate(routeInstance.track[routeInstance.track.length - 1].attributes.timestamp);

    const style: React.CSSProperties = {
      left: `calc(
        ${cssPercent(
          percent(
            currentPos.diff(this.getStart()).as('milliseconds'),
            this.getDuration().as('milliseconds'),
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

    const style: React.CSSProperties = {
      left: `calc(
        ${cssPercent(
          percent(
            DateTime.fromJSDate(timestamp).diff(this.getStart()).as('milliseconds'),
            this.getDuration().as('milliseconds'),
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

    const start = deviation[0];
    const end = deviation[deviation.length - 1];

    const style: React.CSSProperties = {
      left: cssPercent(
        percent(
          DateTime.fromJSDate(start.point.attributes.timestamp).diff(this.getStart()).as('milliseconds'),
          this.getDuration().as('milliseconds'),
        ),
      ),
      right: cssPercent(
        percent(
          this.getEnd().diff(DateTime.fromJSDate(end.point.attributes.timestamp)).as('milliseconds'),
          this.getDuration().as('milliseconds'),
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
