export interface IAppConfig {
  apiUrl: string;
}

export class AppConfigService {

  // tslint:disable-next-line:variable-name
  private _config!: IAppConfig;

  get config() {
    return this._config;
  }

  async init() {
    const x = await fetch('config.json');
    const config = await x.json();
    this._config = config;
    return config;
  }

}
