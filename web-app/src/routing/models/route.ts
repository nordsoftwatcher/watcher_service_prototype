import { Coordinates } from './coordinates';
import { UUID } from './uuid';

export interface IRoute {
  id: UUID;
  name?: string;
  track: Coordinates[];
  checkpoints: ICheckpoint[];
}

export interface ICheckpoint {
  id: UUID;
  name: string;
  address: string;
  description: string;
  coords: Coordinates;
  planTime?: number;
}
