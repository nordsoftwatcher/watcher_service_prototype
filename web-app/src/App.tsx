import React, { Component } from 'react';
// import styles from './App.module.css';

import './icons';

import { DateTime } from 'luxon';

import { RouteInstance } from './routing/components/route-instance/RouteInstance';
import { IRouteInstance } from './routing/models/route-instance';
import { IRoute } from './routing/models/route';
import { IPerson } from './routing/models/person';
import { OperatorRouteApiApi, OperatorDeviceApiApi } from './api/operator-api';
import { mapRoute } from './api-mappers/route';
import { mapRouteInstance } from './api-mappers/route-instance';
import { mapPerson } from './api-mappers/person';
import { UUID } from './routing/models/uuid';

interface IAppState {
  route?: IRoute;
  routeInstance?: IRouteInstance;
  person?: IPerson;
  timestamp: Date;
}

class App extends Component<{}, IAppState> {

  static params = {
    deviceId: 'TEST_DEVICE',
    routeId: 1,
    startTimestamp: '2018-11-23T13:00:00Z',
    refreshIntervalMs: 1000,
  };

  private routeApi = new OperatorRouteApiApi();
  private deviceApi = new OperatorDeviceApiApi();

  constructor(props: {}) {
    super(props);
    this.state = {
      timestamp: new Date(App.params.startTimestamp),
    };
  }

  private async fetchRouteAndPerson() {
    const { supervisor, ...route } = await this.routeApi.getRouteUsingGET(App.params.routeId);

    this.setState({
      route: mapRoute(route),
    });

    if (supervisor) {
      this.setState({
        person: mapPerson(supervisor),
      });
    }

    this.watchRouteInstance();
  }

  private async fetchRouteInstance(routeId: UUID, timestamp: Date) {
    const deviceLocationInfo = await this.deviceApi.getDeviceLocationUsingGET(
      App.params.deviceId,
      undefined,
      routeId,
      timestamp,
    );

    this.setState({
      routeInstance: mapRouteInstance(deviceLocationInfo),
      timestamp: DateTime.fromJSDate(timestamp).plus({ minutes: 5 }).toJSDate(),
    });
  }

  watchRouteInstance() {
    setInterval(async () => {
      const { route, timestamp } = this.state;

      this.fetchRouteInstance(route!.id, timestamp);
    }, App.params.refreshIntervalMs);
  }

  componentDidMount() {
    this.fetchRouteAndPerson();
  }

  render() {
    const { route, routeInstance, person } = this.state;
    if (!route || !person) {
      return null;
    }
    return (
      <RouteInstance route={route} routeInstance={routeInstance} person={person} />
    );
  }
}

export default App;
