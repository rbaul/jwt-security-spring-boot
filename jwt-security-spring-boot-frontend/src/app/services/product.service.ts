import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../products/models/product';
import { Page } from '../models/page';

const API_URL = '/api/products/';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(API_URL);
  }

  getPageableProducts(): Observable<Page<Product>> {
    return this.http.get<Page<Product>>(API_URL + 'pageable');
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(API_URL + id.toString());
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(API_URL, product, httpOptions);
  }

  updateProduct(id: number, product: Product): Observable<any> {
    return this.http.put(API_URL + id.toString(), product, httpOptions);
  }

  deleteProduct(id: number): Observable<Product> {
    return this.http.delete<Product>(API_URL + id.toString(), httpOptions);
  }
}
