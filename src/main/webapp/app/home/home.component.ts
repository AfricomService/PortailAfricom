import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IApplication } from 'app/entities/application/application.model';
import { LANGUAGES } from 'app/config/language.constants';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  account: Account | null = null;
  applications: IApplication[] = [];
  filteredApplications: IApplication[] = [];
  languages = LANGUAGES;
  isLoading = false;
  searchTerm = '';
  isAdmin = false;

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private applicationService: ApplicationService,
    public translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
  this.accountService.identity().subscribe(account => {
    this.account = account;

    this.isAdmin = this.accountService.hasAnyAuthority('ROLE_ADMIN');

    if (account) {
      this.loadApplications();
    }
  });
}

  get fullName(): string {
    if (!this.account) {
      return '';
    }
    const prenom = this.account.firstName ?? '';
    const nom = this.account.lastName ?? '';
    return `${prenom} ${nom}`.trim() || this.account.login;
  }

  get greetingKey(): string {
    const hour = new Date().getHours();
    if (hour < 12) {
      return 'home.greeting.morning';
    }
    if (hour < 18) {
      return 'home.greeting.afternoon';
    }
    return 'home.greeting.evening';
  }

  loadApplications(): void {
    this.isLoading = true;
    this.applicationService.query().subscribe({
      next: res => {
        this.applications = (res.body ?? []).filter(app => app.etat === 'ACTIF');
        this.filteredApplications = this.applications;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    const q = term.trim().toLowerCase();
    this.filteredApplications = !q
      ? this.applications
      : this.applications.filter(app => app.nom?.toLowerCase().includes(q) || app.description?.toLowerCase().includes(q));
  }

  openApplication(app: IApplication): void {
    if (app.url) {
      window.open(app.url, '_blank', 'noopener,noreferrer');
    }
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  trackApp(index: number, app: IApplication): number | undefined {
    return app.id;
  }
}
