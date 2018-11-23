import { Coordinates } from './coordinates';
import { UUID } from './uuid';

export interface IRouteInstance {
  id: UUID;
  personId: UUID;
  chekpoints: ICompletedCheckpoint[];
  track: ITrackCoordiantes[];
  currentPos?: Coordinates;
}

export interface ICompletedCheckpoint {
  pointId: UUID;
  factTime: string;
  arrival: string;
  departure: string;
}

export interface ITrackCoordiantes {
  coords: Coordinates;
  attributes: ITrackCoordinatesMeta;
}

export interface ITrackCoordinatesMeta {
  distanceFromRoute: number;
}
