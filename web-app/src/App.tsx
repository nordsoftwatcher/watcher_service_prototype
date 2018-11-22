import React, { Component } from 'react';
// import styles from './App.module.css';

import './icons';

import { RouteInstance } from './routing/components/route-instance/RouteInstance';
import { IRouteInstance, ITrackCoordiantes } from './routing/models/route-instance';
import { IRoute } from './routing/models/route';
import { IPerson } from './routing/models/person';
import { OperatorApiApi, RouteDto } from './api/operator-api';
import { mapRoute } from './api-mappers/route';

export interface DeviceLocationDto {
  id: number;
  deviceTime: string;
  recordTime: string;
  latitude: number;
  longitude: number;
}

interface IAppState {
  route?: IRoute;
  routeInstance?: IRouteInstance;
  person?: IPerson;
}

class App extends Component<{}, IAppState> {

  private operatorApi: OperatorApiApi = new OperatorApiApi();

  constructor(props: {}) {
    super(props);
    this.state = {
      route: undefined,
    };
  }

  private async fetchRouteAndPerson() {
    const { supervisor, ...route } = await this.operatorApi.getRouteUsingGET(1);

    this.setState({
      route: mapRoute(route),
      person: {
        id: supervisor!.id!,
        lastName: supervisor!.lastName!,
        firstName: supervisor!.name!,
        middleName: supervisor!.middleName!,
      },
    });

    if (supervisor) {
      this.setState({
        person: {
          id: supervisor.id!,
          lastName: supervisor.lastName!,
          firstName: supervisor.name!,
          middleName: supervisor.middleName!,
        },
      });
    }

    this.fetchDeviceLocation(route);
  }

  private async fetchDeviceLocation(route: RouteDto) {
    const deviceLocation: DeviceLocationDto[] = await this.operatorApi.getDeviceLocationUsingGET('TEST_DEVICE');

    const track = deviceLocation.map((loc): ITrackCoordiantes => ({
      coords: {
        lat: loc.latitude,
        lng: loc.longitude,
      },
      attributes: {
        distanceFromRoute: 0,
      },
    }));

    const currentPos = track[track.length - 1];

    this.setState({

      routeInstance: {
        currentPos: currentPos.coords,
        chekpoints: [],
        id: undefined,
        personId: 1,
        track,
      },
    });
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
