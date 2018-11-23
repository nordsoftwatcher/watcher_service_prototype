import { SupervisorDto } from '../api/operator-api';
import { IPerson } from '../routing/models/person';

export function mapPerson(dto: SupervisorDto): IPerson {
  return {
    id: dto.id,
    lastName: dto.lastName!,
    firstName: dto.name!,
    middleName: dto.middleName!,
  };
}
