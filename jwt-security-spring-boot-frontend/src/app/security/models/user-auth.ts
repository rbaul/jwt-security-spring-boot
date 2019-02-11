import { UserClaim } from './user-claim';


export class UserAuth {
  username: string = '';
  bearerToken: string = '';
  authenticated: boolean = false;
  claims: UserClaim[] = [];
}
