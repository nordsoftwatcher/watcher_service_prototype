import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { ConfiguredRouteInstanceContainer } from './routing/components/route-instance/RouteInstance.container';

export const AppRouter = () => (
  <Router>
    <Route path='/routes/:routeId' component={ConfiguredRouteInstanceContainer} />
  </Router>
);
