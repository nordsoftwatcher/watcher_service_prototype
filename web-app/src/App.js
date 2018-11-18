import React, { Component } from 'react';
import './App.css';
import Map from './components/Map';

class App extends Component {
  render() {
    return (
      <div className="App">
        <Map lat={51.505} lon={-0.09} zoom={13}/>
      </div>
    );
  }
}

export default App;
