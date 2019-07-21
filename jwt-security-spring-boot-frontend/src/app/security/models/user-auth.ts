
export class UserAuth {
  username: string = '';
  bearerToken: string = '';
  authenticated: boolean = false;
  roles: String[] = [];
  privileges: String[] = [];
  exp: number;
}

export class UserAuthResponse {
  bearerToken: string;
  authenticated: boolean = false;
  exp: number;
}
