import * as dayjs from 'dayjs';
import { IApplicationCategory } from 'app/entities/application-category/application-category.model';
import { EtatApplication } from 'app/entities/enumerations/etat-application.model';
import { TypeAcces } from 'app/entities/enumerations/type-acces.model';

export interface IApplication {
  id?: number;
  code?: string;
  nom?: string;
  description?: string | null;
  url?: string | null;
  iconContentType?: string | null;
  icon?: string | null;
  couleur?: string | null;
  ordreAffichage?: number | null;
  etat?: EtatApplication;
  typeAcces?: TypeAcces;
  dateCreation?: dayjs.Dayjs | null;
  dateActivation?: dayjs.Dayjs | null;
  dateDesactivation?: dayjs.Dayjs | null;
  categorie?: IApplicationCategory;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public code?: string,
    public nom?: string,
    public description?: string | null,
    public url?: string | null,
    public iconContentType?: string | null,
    public icon?: string | null,
    public couleur?: string | null,
    public ordreAffichage?: number | null,
    public etat?: EtatApplication,
    public typeAcces?: TypeAcces,
    public dateCreation?: dayjs.Dayjs | null,
    public dateActivation?: dayjs.Dayjs | null,
    public dateDesactivation?: dayjs.Dayjs | null,
    public categorie?: IApplicationCategory
  ) {}
}

export function getApplicationIdentifier(application: IApplication): number | undefined {
  return application.id;
}
