import { Coordinates } from '../routing/models/coordinates';
import { IRoute, ICheckpoint } from '../routing/models/route';
import { RouteDto, CheckPoint, RoutePoint } from '../api/operator-api';

export function mapRoute(dto: RouteDto): IRoute {
  return {
    id: dto.id,
    name: '',
    checkpoints: dto.checkPoints!.map(x => mapCheckpoint(x)),
    track: dto.routePoints!.map(x => mapRoutePoint(x)),
  };
}

function mapCheckpoint(dto: CheckPoint): ICheckpoint {
  return {
    id: dto.order,
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

function mapRoutePoint(dto: RoutePoint): Coordinates {
  return {
    lat: dto.latitude!,
    lng: dto.longitude!,
  };
}
