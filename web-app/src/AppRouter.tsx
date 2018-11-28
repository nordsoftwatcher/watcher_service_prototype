import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { RouteInstanceContainer } from './routing/components/route-instance/RouteInstance.container';

export const AppRouter = () => (
  <Router>
    <Route path='/:routeId' component={RouteInstanceContainer} />
  </Router>
);
