import React from 'react';
import styles from './RouteInstance.module.css';

import { IRoute } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance'

import { Panel, Accordion, Badge, Button } from '../../../ui'
import { RoutePoint } from '../route-point/RoutePoint';
import { RouteMap } from '../route-map/RouteMap';
import { RouteTrack } from '../route-track/RouteTrack';

export interface RouteInstanceProps {
  route: IRoute;
  routeInstance: IRouteInstance;
}

interface RouteInstanceState {
  showRoutingPlan: boolean;
}

export class RouteInstance extends React.Component<RouteInstanceProps, RouteInstanceState> {

  state = {
    showRoutingPlan: true
  }

  toggleRoutingPlan = () => {
    this.setState({
      showRoutingPlan: !this.state.showRoutingPlan
    })
  }

  render() {
    const { route, routeInstance } = this.props;
    const { person } = routeInstance;
    const { showRoutingPlan } = this.state;

    const planWidth = '300px';

    return (
      <Panel label='SiWatch Widget'>
        <div className={styles.routeHeader}>
          <div className={styles.routeTitle}>
            {route.name} - {person.lastName} {person.firstName} {person.middleName}
          </div>

          <Badge>В пути</Badge>

          {showRoutingPlan ? (
            <div style={{
              width: planWidth,
              display: 'flex',
              alignItems: 'center'
            }} className='ml-auto'>
              План маршрута
              <div className='ml-auto'>
                <Button link onClick={this.toggleRoutingPlan}>Свернуть</Button>
              </div>
            </div>
          ) : (
              <div className='ml-auto'>
                <Button outline onClick={this.toggleRoutingPlan}>Показать план маршрута</Button>
              </div>

            )}
        </div>
        
        <div className={styles.routeContent}>
          <div className={styles.routeMap}>
            <RouteMap route={route} routeInstance={routeInstance} />
            <RouteTrack route={route} routeInstance={routeInstance} />
          </div>
          {this.state.showRoutingPlan && (
            <div style={{ width: planWidth }} className={styles.routePlan}>
              <Accordion>
                {route.points.map(point => (
                  <RoutePoint
                    point={point}
                    pointInstance={routeInstance.passedPoints.find(p => p.pointId === point.id)}
                    person={routeInstance.person}
                    key={point.id}>
                  </RoutePoint>
                ))}
              </Accordion>
            </div>
          )}
        </div>
      </Panel>
    )
  }
}