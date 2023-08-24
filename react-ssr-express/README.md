# server side rendering using react.js and express.js demo
express. js server to serve two react.js webpages

## run the app
1. run `npm run dev:webpack` to bundle the `react-client-init` for hydrate

2. either run `npm run dev` or debugging using VScode to launch the express.js server app

## demo topic
1. `ReactDOMServer.renderToString` on the server side
2. `ReactDOM.hydrate` to enable react.js at browser side
3. `webpack` + `index.html` to glue topic 1 and 2
4. `ReactDOM.hydrate(<App2 secret='1234'/>, root);` to demo how to pass runtime parameters from server to client
