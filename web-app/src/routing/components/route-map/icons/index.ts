
import { icon } from 'leaflet'

export const passedPointIcon = icon({
  iconUrl: require('./success-point.svg'),
  iconSize: [32, 49],
  iconAnchor: [15, 38]
})

export const routePointIcon = icon({
  iconUrl: require('./route-point.svg'),
  iconSize: [32, 49],
  iconAnchor: [15, 38]
})

export const currentPosIcon = icon({
  iconUrl: require('./current-pos.svg'),
  iconSize: [59, 59],
  iconAnchor: [28, 28]
})
