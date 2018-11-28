import React from 'react';
import styles from './RouteInstance.module.css';
import colors from '../../../colors.module.css';

import Scrollbars from 'react-custom-scrollbars';

import { Panel, Accordion, Badge, Button } from '../../../ui';

import { RoutePoint } from '../route-point/RoutePoint';
import { RouteMap } from '../route-map/RouteMap';
// import { RouteTrack } from '../route-track/RouteTrack';
import { RouteTime } from '../route-time/RouteTime';

import { IRoute, ICheckpoint } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { IPerson } from '../../models/person';
import { UUID } from '../../models/uuid';
import { ITrackEvent } from '../../models/track-event';

export interface RouteInstanceProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
  person: IPerson;
  events: ITrackEvent[];
}

interface RouteInstanceState {
  showRoutingPlan: boolean;
  selectedCheckpointId?: UUID;
}

export class RouteInstance extends React.Component<RouteInstanceProps, RouteInstanceState> {

  private planWidth = '300px';

  constructor(props: RouteInstanceProps) {
    super(props);

    this.state = {
      showRoutingPlan: true,
    };
  }

  toggleRoutingPlan = () => {
    this.setState({
      showRoutingPlan: !this.state.showRoutingPlan,
    });
  }

  handleCheckpointClick = (checkpoint: ICheckpoint) => {
    this.setState({
      selectedCheckpointId: checkpoint.id,
      showRoutingPlan: true,
    });
  }

  renderRoutingPlanHeader() {
    const { showRoutingPlan } = this.state;

    const controlsStyle: React.CSSProperties = {
      width: this.planWidth,
      display: 'flex',
      alignItems: 'center',
    };

    if (showRoutingPlan) {
      return (
        <div style={controlsStyle} className='ml-auto'>
          План маршрута
          <div className='ml-auto'>
            <Button link={true} onClick={this.toggleRoutingPlan}>Свернуть</Button>
          </div>
        </div>
      );
    } else {
      return (
        <div className='ml-auto'>
          <Button outline={true} onClick={this.toggleRoutingPlan}>Показать план маршрута</Button>
        </div>
      );
    }
  }

  renderRoutingPlan() {
    const { route, routeInstance, person } = this.props;
    const { showRoutingPlan, selectedCheckpointId } = this.state;

    if (!showRoutingPlan) {
      return null;
    }

    return (
      <div style={{ width: this.planWidth }} className={styles.routePlan}>
        <Scrollbars
          autoHide={true}
          renderThumbVertical={props =>
            <div {...props} style={{ background: colors.white, opacity: .2, borderRadius: 'inherit' }} />
          }
        >
          <Accordion openItemId={selectedCheckpointId}>
            {route.checkpoints.map(point => (
              <RoutePoint
                point={point}
                pointInstance={routeInstance && routeInstance.chekpoints.find(p => p.pointId === point.id)}
                person={person}
                key={point.id}
              />
            ))}
          </Accordion>
        </Scrollbars>
      </div>
    );
  }

  render() {
    const { route, routeInstance, person, events } = this.props;

    return (
      <Panel label='SiWatch Widget'>
        <div className={styles.routeHeader}>
          <div className={styles.routeTitle}>
            {route.name || 'Маршрут'} - {person.lastName} {person.firstName} {person.middleName}
          </div>

          <Badge>В пути</Badge>

          {this.renderRoutingPlanHeader()}
        </div>

        <div className={styles.routeContent}>
          <div className={styles.routeMap}>
            <RouteMap
              route={route}
              routeInstance={routeInstance}
              onCheckpointClick={this.handleCheckpointClick}
              events={events}
            />
            {/* <RouteTrack route={route} routeInstance={routeInstance} /> */}
            <RouteTime route={route} routeInstance={routeInstance} />
          </div>
          {this.renderRoutingPlan()}
        </div>
      </Panel>
    );
  }
}
