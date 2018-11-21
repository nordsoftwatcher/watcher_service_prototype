import { UUID } from './uuid';

export interface IPerson {
  id: UUID;
  lastName: string;
  firstName: string;
  middleName: string;
}
