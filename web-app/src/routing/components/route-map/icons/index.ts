import L from 'leaflet';
import * as Fa from '@fortawesome/fontawesome-svg-core';
import { faHeartbeat, faHeart, faExclamationCircle, faCircle } from '@fortawesome/free-solid-svg-icons';

import classes from './icons.module.css';

export const passedPointIcon = L.icon({
  iconUrl: require('./success-point.svg'),
  iconSize: [32, 49],
  iconAnchor: [15, 38],
});

export const routePointIcon = L.icon({
  iconUrl: require('./route-point.svg'),
  iconSize: [32, 49],
  iconAnchor: [15, 38],
});

export const currentPosIcon = L.icon({
  iconUrl: require('./current-pos.svg'),
  iconSize: [59, 59],
  iconAnchor: [28, 28],
});

const heartbeat = (className: string) =>
  Fa.layer(push => {
    push(Fa.icon(faHeart, { classes: [] }));
    push(Fa.icon(faHeartbeat, { classes: [className] }));
  });

export const heartRedIcon = L.divIcon({
  html: heartbeat(classes.heartbeatLost).html[0],
  className: 'fa-2x',
  iconSize: [24, 24],
  iconAnchor: [12, 12],
});

export const heartGreenIcon = L.divIcon({
  html: heartbeat(classes.heartbeatFound).html[0],
  className: 'fa-2x',
  iconSize: [24, 24],
  iconAnchor: [12, 12],
});

const sos = Fa.layer(push => {
  push(Fa.icon(faCircle));
  push(Fa.icon(faExclamationCircle, { styles: { color: 'var(--red)' } }));
});

export const sosIcon = L.divIcon({
  html: sos.html[0],
  className: 'fa-2x',
  iconSize: [24, 24],
  iconAnchor: [12, 12],
});
