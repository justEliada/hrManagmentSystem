import { UserResponseDto } from 'src/app/core/models/userResponse.model';

export interface ExtendedUserResponseDto extends UserResponseDto {
  hasPendingVacation?: boolean;
}