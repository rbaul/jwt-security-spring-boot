export class Product {
    id: number;
    name: string;
    description: string;
    price: number;
    state: ProductState;
}

export enum ProductState {
    SOLD_OUT, SALE, REGULAR, COMING_SOON
}
