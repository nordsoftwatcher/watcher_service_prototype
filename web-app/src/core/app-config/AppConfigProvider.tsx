import React from 'react';
import { AppConfigService } from './app-config.service';
import { IAppConfig } from './app-config';

export type IAppConfigContext = IAppConfig | undefined;

export const AppConfigContext = React.createContext<IAppConfigContext>(undefined);

interface AppConfigProviderState {
  config: IAppConfigContext;
}

export class AppConfigProvider extends React.Component<{}, AppConfigProviderState> {

  appConfigService: AppConfigService = new AppConfigService();

  constructor(props: {}) {
    super(props);
    this.state = {
      config: undefined,
    };
  }

  async componentDidMount() {
    await this.appConfigService.init();
    this.setState({
      config: this.appConfigService.config,
    });
  }

  render() {
    const { config } = this.state;

    return (
      <AppConfigContext.Provider value={config}>
        {this.props.children}
      </AppConfigContext.Provider>
    );
  }
}

export interface ConfiguredComponentProps {
  config: IAppConfig;
}

export function withAppConfig<TProps>(Component: React.ComponentType<TProps & ConfiguredComponentProps>) {
  return (props: TProps) => (
    <AppConfigContext.Consumer>
      {config => config ? <Component {...props} config={config} /> : null}
    </AppConfigContext.Consumer>
  );
}
