import React, { Component } from 'react';
import styles from './App.module.css';
import { RouteInstance } from './routing/components/route-instance/RouteInstance';
import { IRouteInstance } from './routing/models/route-instance';
import { IRoute } from './routing/models/route';
import { IPerson } from './routing/models/person';
import { LatLngTuple } from 'leaflet';

const route: IRoute = {
  id: '1',
  name: 'Маршрут №1234',
  points: [
    {
      id: '1',
      name: 'Пункт назначения №1',
      coord: [57.9911, 40.1803],
      address: 'ул. Красного текстильщика, дом 34, корпус 3',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis facilisis mauris at dapibus sodales. Maecenas interdum elementum consectetur...',
    },
    {
      id: '2',
      name: 'Пункт назначения №2',
      coord: [57.9973, 40.1974],
      address: 'ул. Красного текстильщика, дом 34, корпус 3',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis facilisis mauris at dapibus sodales. Maecenas interdum elementum consectetur...',
    },
    {
      id: '3',
      name: 'Пункт назначения №3',
      coord: [57.9819, 40.2389],
      address: 'ул. Красного текстильщика, дом 34, корпус 3',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis facilisis mauris at dapibus sodales. Maecenas interdum elementum consectetur...',
    },
    {
      id: '4',
      name: 'Пункт назначения №4',
      coord: [57.9763, 40.2579],
      address: 'ул. Красного текстильщика, дом 34, корпус 3',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis facilisis mauris at dapibus sodales. Maecenas interdum elementum consectetur...',
    },
    {
      id: '5',
      name: 'Пункт назначения №5',
      coord: [57.9802, 40.2733],
      address: 'ул. Красного текстильщика, дом 34, корпус 3',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis facilisis mauris at dapibus sodales. Maecenas interdum elementum consectetur...',
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
    [40.234433, 57.984592],
    [40.239047, 57.981987],
    [40.243027, 57.979484],
    [40.252769, 57.976776],
    [40.258176, 57.976321],
    [40.268347, 57.974409],
    [40.274227, 57.973908],
    [40.2742, 57.974147],
    [40.27346, 57.974768],
    [40.273245, 57.975189],
    [40.274017, 57.975337],
    [40.274597, 57.975627],
    [40.27413, 57.976116],
    [40.27074, 57.978027],
    [40.270439, 57.978414],
    [40.271421, 57.978813],
    [40.273427, 57.980212],
  ].map(([y, x]) => [x, y] as LatLngTuple)
}

const person: IPerson = {
  lastName: 'Иванов',
  firstName: 'Иван',
  middleName: 'Иванович',
}

const routeInstance: IRouteInstance = {
  person,
  currentPos: [57.9891, 40.2288],
  passedPoints: [
    {
      pointId: '1',
      arrival: '09:13',
      departure: '10:02',
    },
    {
      pointId: '2',
      arrival: '10:42',
      departure: '10:58',
    }
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
  ].map(([y, x]) => [x, y] as LatLngTuple)
}

class App extends Component {
  render() {
    return (
      <RouteInstance route={route} routeInstance={routeInstance} />
    );
  }
}

export default App;
