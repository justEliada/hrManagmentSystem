import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface ToastMessage {
  content: string;
  style?: 'success' | 'error' | 'info';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toastMessages: Subject<ToastMessage> = new Subject<ToastMessage>();

  constructor() { }

  show(content: string, style: 'success' | 'error' | 'info' = 'info') {
    this.toastMessages.next({ content, style });
  }
}