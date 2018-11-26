export function mapDate(date?: string | Date): Date | undefined {
  if (date) {
    if (typeof date === 'string') {
      return new Date(date);
    }
    return new Date(date);
  } else {
    return undefined;
  }
}
