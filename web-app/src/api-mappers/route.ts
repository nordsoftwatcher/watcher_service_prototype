import { Coordinates } from '../routing/models/coordinates';
import { IRoute, ICheckpoint } from '../routing/models/route';
import { RouteDto, CheckPoint, RoutePoint } from '../api/operator-api';
import { mapDate } from './date';

export function mapRoute(dto: RouteDto): IRoute {
  return {
    id: dto.id,
    name: dto.name,
    checkpoints: dto.checkPoints!.map(x => mapCheckpoint(x)),
    track: dto.routePoints!.map(x => mapRoutePoint(x)),
  };
}

function mapCheckpoint(dto: CheckPoint): ICheckpoint {
  return {
    id: dto.id,
    address: dto.address!,
    description: dto.description!,
    name: dto.name!,
    coords: {
      lat: dto.latitude!,
      lng: dto.longitude!,
    },
    planArrival: mapDate(dto.arrivalTime)!,
    planDeparute: mapDate(dto.departureTime),
  };
}

function mapRoutePoint(dto: RoutePoint): Coordinates {
  return {
    lat: dto.latitude!,
    lng: dto.longitude!,
  };
}
