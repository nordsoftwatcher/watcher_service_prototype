import { Coordinates } from './coordinates';
import { UUID } from './uuid';

export interface ITrackEvent {
  id: UUID;
  coords: Coordinates;
  timestamp: Date;
  type: string;
  value: string;
}
