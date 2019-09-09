import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { Page } from '../../app-security-module/models/page';
import { PageableApiService } from 'src/app/app-security-module/services/pageable-api-service';

const API_URL = '/api/products';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class ProductService extends PageableApiService<Product> {

  constructor(http: HttpClient) {
    super(http, API_URL);
   }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(API_URL);
  }

  getPageableProducts(): Observable<Page<Product>> {
    return this.http.get<Page<Product>>(API_URL);
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(API_URL + '/' + id.toString());
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(API_URL, product, httpOptions);
  }

  updateProduct(id: number, product: Product): Observable<any> {
    return this.http.put(API_URL + '/' + id.toString(), product, httpOptions);
  }

  deleteProduct(id: number): Observable<Product> {
    return this.http.delete<Product>(API_URL + '/' + id.toString(), httpOptions);
  }
}
