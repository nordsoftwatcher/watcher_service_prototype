import { Coordinates } from './coordinates';
import { UUID } from './uuid';

export interface IRouteInstance {
  id: UUID;
  chekpoints: ICompletedCheckpoint[];
  track: ITrackCoordiantes[];
  currentPos?: Coordinates;
}

export interface ICompletedCheckpoint {
  pointId: UUID;
  arrival: Date;
  departure?: Date;
  factTime?: number;
}

export interface ITrackCoordiantes {
  coords: Coordinates;
  attributes: ITrackCoordinatesMeta;
}

export interface ITrackCoordinatesMeta {
  distanceFromRoute: number;
}
