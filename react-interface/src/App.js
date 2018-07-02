import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import TableRest from './components/table.js';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">Projeto SD</h1>
        </header>
        <br/>
        <br/>
        <TableRest></TableRest>
      </div>
    );
  }
}

export default App;
