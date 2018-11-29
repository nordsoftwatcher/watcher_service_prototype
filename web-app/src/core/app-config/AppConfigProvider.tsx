import React from 'react';
import { IAppConfig, AppConfigService } from './app-config.service';

export const AppConfigContext = React.createContext<IAppConfig | undefined>(undefined);

export class AppConfigProvider extends React.Component<{}, { config: IAppConfig | undefined }> {

  appConfigService: AppConfigService = new AppConfigService();

  constructor(props: {}) {
    super(props);
    this.state = {
      config: undefined,
    };
  }

  async componentDidMount() {
    const config = await this.appConfigService.init();
    this.setState({
      config,
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
      {config => {
        if (!config) {
          return null;
        } else {
          return <Component config={config} {...props} />;
        }
      }}
    </AppConfigContext.Consumer>
  );
}
