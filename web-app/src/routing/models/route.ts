export interface IRoute {
  id: string;
  name: string;
  points: IRoutePoint[];
  track: [number, number][];
}

export interface IRoutePoint {
  id: string;
  name: string;
  address: string;
  description: string;
  coord: [number, number];
}