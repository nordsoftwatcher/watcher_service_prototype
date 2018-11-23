import React from 'react';
import styles from './RouteInstance.module.css';

import Scrollbars from 'react-custom-scrollbars';

import { Panel, Accordion, Badge, Button } from '../../../ui';

import { RoutePoint } from '../route-point/RoutePoint';
import { RouteMap } from '../route-map/RouteMap';
import { RouteTrack } from '../route-track/RouteTrack';

import { IRoute } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { IPerson } from '../../models/person';

export interface RouteInstanceProps {
  route: IRoute;
  routeInstance?: IRouteInstance;
  person: IPerson;
}

interface RouteInstanceState {
  showRoutingPlan: boolean;
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
    const { showRoutingPlan } = this.state;

    if (!showRoutingPlan) {
      return null;
    }

    return (
      <div style={{ width: this.planWidth }} className={styles.routePlan}>
        <Scrollbars autoHide={true}>
          <Accordion>
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
    const { route, routeInstance, person } = this.props;

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
            <RouteMap route={route} routeInstance={routeInstance} />
            <RouteTrack route={route} routeInstance={routeInstance} />
          </div>
          {this.renderRoutingPlan()}
        </div>
      </Panel>
    );
  }
}
