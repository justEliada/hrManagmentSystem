export class User {
    id!: number;
    name!: string;
    username!: string;
    role!: 'User' | 'Manager';
    password!: string; 
    vacationStatus!: 'Approved' | 'Pending' | 'Rejected';
    position!: string;
  }
 