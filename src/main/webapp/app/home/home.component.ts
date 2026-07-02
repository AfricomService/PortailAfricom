import { Component, OnInit } from '@angular/core';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IApplication } from 'app/entities/application/application.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  account: Account | null = null;
  applications: IApplication[] = [];

  constructor(private accountService: AccountService, private loginService: LoginService, private applicationService: ApplicationService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.account = account;
      if (account) {
        this.loadApplications();
      }
    });
  }

  login(): void {
    this.loginService.login();
  }

  loadApplications(): void {
    this.applicationService.query().subscribe(res => {
      this.applications = (res.body ?? []).filter(app => app.etat === 'ACTIF');
    });
  }

  openApplication(app: IApplication): void {
    if (app.url) {
      window.open(app.url, '_blank', 'noopener,noreferrer');
    }
  }
}
