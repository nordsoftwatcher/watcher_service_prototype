import React, { Component } from 'react';
// import styles from './App.module.css';

import './icons';

import { RouteInstance } from './routing/components/route-instance/RouteInstance';
import { IRouteInstance } from './routing/models/route-instance';
import { IRoute } from './routing/models/route';
import { IPerson } from './routing/models/person';
import { RouteApiApi } from './api';
import { mapRoute } from './api-mappers/route';

const person: IPerson = {
  id: 1,
  lastName: 'Иванов',
  firstName: 'Иван',
  middleName: 'Иванович',
};

/* const routeInstance: IRouteInstance = {
  id: '1',
  personId: '1',
  currentPos: [57.9891, 40.2288],
  chekpoints: [
    {
      pointId: '1',
      factTime: '00:00',
      arrival: '09:13',
      departure: '10:02',
    },
    {
      pointId: '2',
      factTime: '00:00',
      arrival: '10:42',
      departure: '10:58',
    },
  ],
  track: [
    [40.180113, 57.990973],
    [40.172131, 57.993134],
    [40.17153, 57.993998],
    [40.172346, 57.997797],
    [40.172818, 57.999548],
    [40.170114, 58.000526],
    [40.167668, 58.000367],
    [40.166681, 58.002959],
    [40.175393, 58.001958],
    [40.185778, 58.002027],
    [40.188138, 57.999639],
    [40.190027, 57.999184],
    [40.197537, 57.997546],
    [40.205691, 57.997296],
    [40.210669, 57.995841],
    [40.221527, 57.992884],
    [40.228693, 57.989358],
  ]
    .map(([y, x]) => [x, y] as LatLngTuple)
    .map((coords): ITrackCoordiantes => ({
      coords,
      attributes: {
        distanceFromRoute: 0,
      },
    })),
}; */

interface IAppState {
  route?: IRoute;
  routeInstance?: IRouteInstance;
}

class App extends Component<{}, IAppState> {

  private api: RouteApiApi = new RouteApiApi();

  constructor(props: {}) {
    super(props);
    this.state = {
      route: undefined,
    };
  }

  componentDidMount() {
    this.api.getRouteUsingGET(2).then(
      res => {
        this.setState({
          route: mapRoute(res),
        });
      },
    );
  }

  render() {
    const { route, routeInstance } = this.state;
    if (!route) {
      return null;
    }
    return (
      <RouteInstance route={route} routeInstance={routeInstance} person={person} />
    );
  }
}

export default App;
