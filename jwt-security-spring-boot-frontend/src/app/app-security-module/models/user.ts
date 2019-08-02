import { RoleResponseDto } from './role';

export class User {
    username: string;
    password: string;

    constructor(username: string, password: string) {
        this.username = username;
        this.password = password;
    }
}

export class UserResponseDto {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    roles: RoleResponseDto[];
}

export class UserRequestDto {
    // id: number;
    username: string;
    password: string;
    firstName: string;
    lastName: string;
    roleIds: number[];
}
