import { Observable } from 'rxjs';
import { ActivityLog } from './activity-log';
import { ActivityLogApiService } from '../services/activity-log-api.service';
import { Page } from 'src/app/app-security-module/models/page';
import { PageableDataSource } from '../../app-security-module/services/pageable.datasource';

export class ActivityLogDataSource extends PageableDataSource<ActivityLog> {

    constructor(private activityLogApiService: ActivityLogApiService) {
        super();
    }

    getPageableContent(pageSize: number, pageIndex: number, sort: string[], filter: string): Observable<Page<ActivityLog>> {
        return this.activityLogApiService.search(pageSize, pageIndex, sort, filter);
    }

}
