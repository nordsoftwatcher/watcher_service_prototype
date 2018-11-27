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
}

export interface ITrackCoordiantes {
  coords: Coordinates;
  attributes: ITrackCoordinatesMeta;
}

export interface ITrackCoordinatesMeta {
  timestamp: Date;
  distanceFromRoute: number;
}
