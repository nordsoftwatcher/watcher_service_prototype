import { IAppConfig } from './app-config';

export class AppConfigService {

  // tslint:disable-next-line:variable-name
  private _config: IAppConfig | undefined;

  get config() {
    return this._config;
  }

  async init() {
    const x = await fetch('/config.json');
    const data = await x.json();
    this._config = this.parseConfig(data);
    return this._config;
  }

  private parseConfig(data: any) {
    return data as IAppConfig;
  }

}
