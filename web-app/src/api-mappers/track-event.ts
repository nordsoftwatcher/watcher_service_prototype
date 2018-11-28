import { Event } from '../api/operator-api';
import { ITrackEvent } from '../routing/models/track-event';
import { mapDate } from './date';

export function mapEvent(dto: Event): ITrackEvent {
  return {
    id: dto.id,
    coords: {
      lat: dto.latitude!,
      lng: dto.longitude!,
    },
    timestamp: mapDate(dto.deviceTime)!,
    type: dto.eventType!,
    value: dto.eventValue!,
  };
}
