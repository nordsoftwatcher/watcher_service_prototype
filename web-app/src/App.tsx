import React, { Component } from 'react';
import classes from './App.module.css';

import './icons';

import { DateTime } from 'luxon';

import { RouteInstance } from './routing/components/route-instance/RouteInstance';
import { RouteReport } from './routing/components/route-report/RouteReport';

import { IRouteInstance } from './routing/models/route-instance';
import { IRoute } from './routing/models/route';
import { IPerson } from './routing/models/person';
import { OperatorRouteApiApi, OperatorDeviceApiApi, OperatorEventApiApi } from './api/operator-api';
import { mapRoute } from './api-mappers/route';
import { mapRouteInstance } from './api-mappers/route-instance';
import { mapPerson } from './api-mappers/person';
import { UUID } from './routing/models/uuid';
import { ITrackEvent } from './routing/models/track-event';
import { mapEvent } from './api-mappers/track-event';

interface IAppState {
  route?: IRoute;
  routeInstance?: IRouteInstance;
  events: ITrackEvent[];
  person?: IPerson;
  timestamp?: Date;
}

class App extends Component<{}, IAppState> {

  static params = {
    deviceId: 'TEST_DEVICE',
    routeId: 4,
    refreshIntervalMs: 1000,
  };

  private routeApi = new OperatorRouteApiApi();
  private deviceApi = new OperatorDeviceApiApi();
  private eventsApi = new OperatorEventApiApi();

  constructor(props: {}) {
    super(props);
    this.state = {
      events: [],
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
    this.watchEvents();
  }

  private async fetchRouteInstance(routeId: UUID, timestamp?: Date) {
    const deviceLocationInfo = await this.deviceApi.getDeviceLocationUsingGET(
      App.params.deviceId,
      undefined,
      routeId,
      timestamp,
    );

    this.setState({
      routeInstance: mapRouteInstance(deviceLocationInfo),
      timestamp: timestamp && DateTime.fromJSDate(timestamp).plus({ minutes: 5 }).toJSDate() || undefined,
    });
  }

  private watchRouteInstance() {
    setInterval(async () => {
      const { route, timestamp } = this.state;

      this.fetchRouteInstance(route!.id, timestamp);
    }, App.params.refreshIntervalMs);
  }

  private async fetchEvents() {
    const { person } = this.state;

    if (!person) {
      return;
    }

    const events = await this.eventsApi.getEventUsingGET(
      undefined,
      person.id,
      undefined,
    );

    this.setState({
      events: events.filter(x => !!x.latitude && !!x.longitude).map(mapEvent),
    });
  }

  private watchEvents() {
    setInterval(async () => {
      this.fetchEvents();
    }, App.params.refreshIntervalMs);
  }

  componentDidMount() {
    this.fetchRouteAndPerson();
  }

  render() {
    const { route, routeInstance, person, events } = this.state;
    if (!route || !person) {
      return null;
    }
    return (
      <div className={classes.root}>
        <div className={classes.routeWidget}>
          <RouteInstance route={route} routeInstance={routeInstance} person={person} events={events} />
        </div>
        {/* <div className={classes.reportWidget}>
          {routeInstance && <RouteReport route={route} routeInstance={routeInstance} />}
        </div> */}
      </div>
    );
  }
}

export default App;
