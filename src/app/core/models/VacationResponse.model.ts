export class VacationResponse {
    id!: number;
    fromDate!: Date;
    toDate!: Date;
    notes!: string;
    createdBy!: string;
    userId!: number;
    status!: 'PENDING' | 'APPROVED' | 'REJECTED';
  }