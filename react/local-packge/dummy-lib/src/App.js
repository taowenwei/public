import logo from './logo.svg';
import './App.css';
import { Dummy } from './lib/index';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <Dummy/>
      </header>
    </div>
  );
}

export default App;
