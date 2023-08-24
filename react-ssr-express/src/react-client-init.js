import React from 'react';
import ReactDOM from 'react-dom';
import App1 from './components/app1';
import App2 from './components/app2';

let root = document.getElementById('app1');
if (root) {
    ReactDOM.hydrate(<App1 />, root);
}

root = document.getElementById('app2');
if (root) {
    ReactDOM.hydrate(<App2 secret='1234'/>, root);
}