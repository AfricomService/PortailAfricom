import { IApplication } from 'app/entities/application/application.model';

export interface IApplicationCategory {
  id?: number;
  code?: string;
  nom?: string;
  description?: string | null;
  ordreAffichage?: number | null;
  applications?: IApplication[] | null;
}

export class ApplicationCategory implements IApplicationCategory {
  constructor(
    public id?: number,
    public code?: string,
    public nom?: string,
    public description?: string | null,
    public ordreAffichage?: number | null,
    public applications?: IApplication[] | null
  ) {}
}

export function getApplicationCategoryIdentifier(applicationCategory: IApplicationCategory): number | undefined {
  return applicationCategory.id;
}
