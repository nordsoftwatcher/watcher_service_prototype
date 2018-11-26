import { IRouteInstance, ICompletedCheckpoint, ITrackCoordiantes } from '../routing/models/route-instance';
import { DeviceLocationOutput, CheckPointResultDto, LocationDto } from '../api/operator-api';
import { mapDate } from './date';

export function mapRouteInstance(dto: DeviceLocationOutput): IRouteInstance {
  const track = dto.locations ? dto.locations.map(mapLocation) : [];
  const currentPos = track.length && track[track.length - 1].coords || undefined;

  return {
    id: undefined,
    chekpoints: dto.checkpoints ? dto.checkpoints.map(mapCheckpoint) : [],
    track,
    currentPos,
  };
}

function mapCheckpoint(dto: CheckPointResultDto): ICompletedCheckpoint {
  return {
    pointId: dto.id,
    arrival: mapDate(dto.arrivalTime)!,
    departure: mapDate(dto.departureTime),
  };
}

function mapLocation(dto: LocationDto): ITrackCoordiantes {
  return {
    coords: {
      lat: dto.latitude!,
      lng: dto.longitude!,
    },
    attributes: {
      timestamp: mapDate(dto.deviceTime)!,
      distanceFromRoute: dto.routeDistance!,
    },
  };
}
