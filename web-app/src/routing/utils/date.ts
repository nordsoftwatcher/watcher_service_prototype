import { DateTime, Duration } from 'luxon';

export function formatTime(datetime?: Date | string) {
  if (datetime) {
    if (typeof datetime === 'string') {
      return DateTime.fromISO(datetime).toFormat('HH:mm');
    } else {
      return DateTime.fromJSDate(datetime).toFormat('HH:mm');
    }
  } else {
    return '-';
  }
}

export function formatMinutes(minutes?: number) {
  if (minutes) {
    return Duration.fromObject({ minutes }).toFormat('hh:mm');
  } else {
    return '-';
  }
}
