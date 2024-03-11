import { Injectable } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class ImageFormDetailsService {

  constructor(private fb: FormBuilder) { }

  createImageDetailsForm(): FormGroup {
    return this.fb.group({
      name: [null, [Validators.required, Validators.maxLength(80)]],
      customUri: [null, [Validators.required, this.uriValidator()]]
    })
  }

  private uriValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const isValid = /^([a-z0-9]+-)*[a-z0-9]+$/.test(control.value);
      return isValid ? null : { uriValidation: true };
    }
  }
}
