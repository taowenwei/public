import express from 'express';
import path from 'path';
import fs from 'fs';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import App1 from './components/app1';
import App2 from './components/app2';

const app = express();

app.use(express.static('public'));

const renderReactComponent = (res, rootId, htmlPage, component) => {
  fs.readFile(path.resolve(htmlPage), 'utf8', (err, data) => {
    if (err) {
      console.error('Something went wrong:', err);
      return res.status(500);
    }

    return res.send(
      data.replace('<div id="root"></div>', `<div id="${rootId}">${ReactDOMServer.renderToString(component)}</div>`)
    );
  });
};

app.use('/app1', (_, res) => {
  renderReactComponent(res, 'app1', './public/index.html', <App1 />);
});

app.use('/app2', (_, res) => {
  renderReactComponent(res, 'app2', './public/index.html', <App2 />);
});

const port = process.env.PORT || 3030;
app.listen(port, function listenHandler() {
  console.info(`Running on ${port}...`);
});
