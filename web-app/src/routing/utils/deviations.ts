import { IRouteInstance, ITrackCoordiantes } from '../models/route-instance';

export type Deviation = Array<{ point: ITrackCoordiantes; index: number }>;

const deviationThreshold = 0.0003;

export function getDeviations(routeInstance?: IRouteInstance) {
  if (!routeInstance) {
    return [];
  }

  const indexedPoints = routeInstance.track.map((point, index) => ({ point, index }));
  const deviatePoints = indexedPoints.filter(x => x.point.attributes.distanceFromRoute > deviationThreshold);

  const deviateLines = deviatePoints.reduce((acc, currPoint) => {
    const lastLine = acc[acc.length - 1];
    if (!lastLine) {
      acc.push([currPoint]);
    } else {
      const lastPoint = lastLine[lastLine.length - 1];
      const indexDiff = currPoint.index - lastPoint.index;
      if (indexDiff < 3) {
        const toAdd = indexedPoints.slice(lastPoint.index + 1, currPoint.index + 1);
        lastLine.push(...toAdd);
      } else {
        acc.push([currPoint]);
      }
    }
    return acc;
  }, [] as Deviation[]);

  /* deviateLines.forEach(line => {
    const first = line[0];
    const beforeFirst = routeInstance.track[first.index - 1];
    if (beforeFirst) {
      line.unshift({ point: beforeFirst, index: first.index - 1 });
    }
    const last = line[line.length - 1];
    const afterLast = routeInstance.track[last.index + 1];
    if (afterLast) {
      line.push({ point: afterLast, index: last.index + 1 });
    }
  }); */

  return deviateLines;
}
