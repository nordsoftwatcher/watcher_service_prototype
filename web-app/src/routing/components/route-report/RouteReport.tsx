import React from 'react';

import classes from './RouteReport.module.css';

import { IRoute } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { Panel } from '../../../ui';

interface RouteReportProps {
  route: IRoute;
  routeInstance: IRouteInstance;
}

export class RouteReport extends React.Component<RouteReportProps> {
  render() {
    return (
      <Panel label='Route report'>
        Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quod repellat aut aliquam quasi unde beatae laboriosam autem fuga porro, sequi, laborum quam commodi numquam suscipit iure atque, quae fugiat accusantium.
      </Panel>
    );
  }
}
