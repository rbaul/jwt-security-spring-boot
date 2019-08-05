export class ActivityLog {
    id: number;
    username: string;
    action: string;
    time: Date;
    status: ActivityLogStatus;
}

export enum ActivityLogStatus {
    SUCCESS = 'SUCCESS',
    FAILED = 'FAILED'
}
