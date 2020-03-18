import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BankComponent } from './bank.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { RouterTestingModule } from '@angular/router/testing';
import { NavbarComponent } from '../common/navbar/navbar.component';
import { AuthService } from '../services/auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BankProfileService } from '../bank-search/services/bank-profile.service';
import { BankProfile } from '../api';
import { of } from 'rxjs';

describe('BankComponent', () => {
  let component: BankComponent;
  let fixture: ComponentFixture<BankComponent>;
  const authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn']);
  const bankService: BankProfileService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SidebarComponent, BankComponent, NavbarComponent],
      imports: [RouterTestingModule, HttpClientTestingModule, ReactiveFormsModule],
      providers: [bankService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BankComponent);
    component = fixture.componentInstance;
    authServiceSpy.isLoggedIn.and.returnValue(true);
    fixture.detectChanges();
  });

  it('should create', () => {
    const mockBankProfile: BankProfile = {
      bankId: 'xxxxxxxx',
      bankName: 'adorsys',
      bic: 'string-bic',
      services: ['']
    } as BankProfile;

    const getBankProfileSpy = spyOn(bankService, 'getBankProfile').and.returnValue(of(mockBankProfile));
    expect(getBankProfileSpy).toHaveBeenCalled();
    component.ngOnInit();
    expect(component).toBeTruthy();
  });
});
