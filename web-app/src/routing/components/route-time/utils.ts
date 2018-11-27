
export function percent(value: number, total: number) {
  return value / total * 100;
}

export const cssLength = (unit: string) => (value: number) => `${value}${unit}`;
export const cssPercent = cssLength('%');
