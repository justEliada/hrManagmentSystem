import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent {
  @Input() show: boolean = false;
  @Output() showChange = new EventEmitter<boolean>();

  close() {
    this.show = false;
    this.showChange.emit(this.show);
  }
}