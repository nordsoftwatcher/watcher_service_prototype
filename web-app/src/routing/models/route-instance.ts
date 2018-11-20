import { IPerson } from "./person";

export interface IRouteInstance {
  passedPoints: IRouteInstacePoint[];
  currentPos: [number, number];
  track: [number, number][];
  person: IPerson;
}

export interface IRouteInstacePoint {
  pointId: string;
  arrival: string;
  departure: string;
}