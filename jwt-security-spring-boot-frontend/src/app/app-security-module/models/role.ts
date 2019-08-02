export class RoleResponseDto {
    id: number;
    name: string;
    description: string;
    privileges: PrivilegeResponseDto[];
}

export class RoleRequestDto {
    id?: number;
    name: string;
    description: string;
    privilegeIds: number[];
}

export class PrivilegeResponseDto {
    id: number;
    name: string;
    description: string;
}
