import React, { Component } from 'react';
import classes from './App.module.css';

import './icons';

import { AppRouter } from './AppRouter';

class App extends Component {

  render() {
    return (
      <div className={classes.root}>
        <div className={classes.routeWidget}>
          <AppRouter />
        </div>
        {/* <div className={classes.reportWidget}>
          {routeInstance && <RouteReport route={route} routeInstance={routeInstance} />}
        </div> */}
      </div>
    );
  }
}

export default App;
