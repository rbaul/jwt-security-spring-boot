export class ErrorDto {
    errorCode: string;
    message: string;
    timestamp: Date;
    errors: any[];
}

export class ValidationErrorDto {
    fieldName: string;
    errorCode: string;
    rejectedValue: string;
    params: any[];
    message: string;
}
