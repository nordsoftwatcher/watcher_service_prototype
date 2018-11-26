export function mapDate(date?: string | Date): Date | undefined {
  if (date) {
    if (typeof date === 'string') {
      return new Date(appendUtcTimezoneIfNoTimezone(date));
    }
    return new Date(date);
  } else {
    return undefined;
  }
}

function hasTimezone(date: string) {
  return /Z$|\+0000$/.test(date);
}

function appendUtcTimezone(date: string) {
  return date + 'Z';
}

function appendUtcTimezoneIfNoTimezone(date: string) {
  if (hasTimezone(date)) {
    return date;
  } else {
    return appendUtcTimezone(date);
  }
}
