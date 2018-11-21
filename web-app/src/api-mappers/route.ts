import { RouteDto, CheckPointDto, RoutePointDto } from '../api/api';
import { IRoute, ICheckpoint } from '../routing/models/route';
import { Coordinates } from '../routing/models/coordinates';

export function mapRoute(dto: RouteDto): IRoute {
  return {
    id: dto.id,
    name: '',
    checkpoints: dto.checkPoints!.map(x => mapCheckpoint(x)),
    track: dto.routePoints!.map(x => mapRoutePoint(x)),
  };
}

function mapCheckpoint(dto: CheckPointDto): ICheckpoint {
  return {
    id: dto.id,
    address: dto.address!,
    description: dto.description!,
    name: dto.name!,
    planTime: String(dto.planTime),
    coords: {
      lat: dto.latitude!,
      lng: dto.longitude!,
    },
  };
}

function mapRoutePoint(dto: RoutePointDto): Coordinates {
  return {
    lat: dto.latitude!,
    lng: dto.longitude!,
  };
}
