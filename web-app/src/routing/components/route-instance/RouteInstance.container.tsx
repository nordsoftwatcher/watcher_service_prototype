import React from 'react';
import { RouteComponentProps } from 'react-router-dom';

import { RouteInstance } from './RouteInstance';

import { IRoute } from '../../models/route';
import { IRouteInstance } from '../../models/route-instance';
import { ITrackEvent } from '../../models/track-event';
import { IPerson } from '../../models/person';
import { UUID } from '../../models/uuid';

import { mapRoute } from '../../../api-mappers/route';
import { mapPerson } from '../../../api-mappers/person';
import { mapRouteInstance } from '../../../api-mappers/route-instance';
import { mapEvent } from '../../../api-mappers/track-event';

import { OperatorRouteApiApi, OperatorDeviceApiApi, OperatorEventApiApi } from '../../../api/operator-api';
import { withAppConfig, ConfiguredComponentProps } from '../../../core/app-config/AppConfigProvider';

const PARAMS = {
  deviceId: 'TEST_DEVICE',
  refreshIntervalMs: 1000,
};

interface RouteParams {
  routeId: string;
}

// tslint:disable-next-line:no-empty-interface
interface RouteInstanceContainerProps {

}

type Props = RouteInstanceContainerProps & ConfiguredComponentProps & RouteComponentProps<RouteParams>;

interface RouteInstanceContainerState {
  route?: IRoute;
  routeInstance?: IRouteInstance;
  events: ITrackEvent[];
  person?: IPerson;
}

export class RouteInstanceContainer extends React.Component<Props, RouteInstanceContainerState> {

  private routeApi: OperatorRouteApiApi;
  private deviceApi: OperatorDeviceApiApi;
  private eventsApi: OperatorEventApiApi;

  constructor(props: Props) {
    super(props);

    const { config } = this.props;

    this.routeApi = new OperatorRouteApiApi(undefined, config.apiUrl);
    this.deviceApi = new OperatorDeviceApiApi(undefined, config.apiUrl);
    this.eventsApi = new OperatorEventApiApi(undefined, config.apiUrl);

    this.state = {
      events: [],
    };
  }

  componentDidMount() {
    const { match } = this.props;
    const routeId = parseInt(match.params.routeId, 10);
    this.fetchRouteAndPerson(routeId);
  }

  private async fetchRouteAndPerson(routeId: number) {
    const { supervisor, ...route } = await this.routeApi.getRouteUsingGET(routeId);

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

  private async fetchRouteInstance(routeId: UUID) {
    const deviceLocationInfo = await this.deviceApi.getDeviceLocationUsingGET(
      PARAMS.deviceId,
      undefined,
      routeId,
    );

    this.setState({
      routeInstance: mapRouteInstance(deviceLocationInfo),
    });
  }

  private watchRouteInstance() {
    setInterval(async () => {
      const { route } = this.state;

      this.fetchRouteInstance(route!.id);
    }, PARAMS.refreshIntervalMs);
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
    }, PARAMS.refreshIntervalMs);
  }

  static params = {
    deviceId: 'TEST_DEVICE',
    refreshIntervalMs: 1000,
  };

  render() {
    const { route, routeInstance, person, events } = this.state;
    if (!route || !person) {
      return null;
    }

    return (
      <RouteInstance route={route} routeInstance={routeInstance} person={person} events={events} />
    );
  }
}

export const ConfiguredRouteInstanceContainer = withAppConfig(RouteInstanceContainer);
