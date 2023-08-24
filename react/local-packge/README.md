# Test react.js local component package

## Component package - dummy-lib
ref: https://levelup.gitconnected.com/publish-react-components-as-an-npm-package-7a671a2fb7f

ref: https://medium.com/geekculture/how-to-test-a-local-react-npm-package-still-on-development-9bed7f199164

1. Use `npx create-react-app` to create a react.js app as your library 
    1. You can keep using `npm start` to run it as a standalone app for developing your components
2. Prepare a folder (e.g. `lib`)for your library components and export them all in an index.js file
3. Prepare Babel build with a `babel.config.json`, and install Babel
    ```bash 
    npm install --save-dev @babel/core @babel/cli @babel/preset-env 
    npm install -save @babel/polyfill
    ```
4. Update `package.json`
    1. build script: 
    ```yaml
    "build:lib": "rm -rf dist && NODE_ENV=production babel src/lib --out-dir dist --copy-files"
    ```
    2. Module files:
    ```yaml
    "main": "dist/lib/index.js",
    "module": "dist/index.js",
    "files": [ "dist"],
    ```
5. Build your library, `npm run build:lib`

## Sample app - dummy-app
1. Install the local package
```bash
npm install ../dummy-lib
```
2. Remove the local package's node_modules folder
```bash
rm -rf node_modules/dummy-lib/node_modules
```
3. Run you app with `npm start`


## The reason for such elaborated setup
If not removing the local package's `node_modules`, when launch the app, the error below will be reported in a browser - `...more than one copy of React...`


```
Error: Invalid hook call. Hooks can only be called inside of the body of a function component. This could happen for one of the following reasons:
1. You might have mismatching versions of React and the renderer (such as React DOM)
2. You might be breaking the Rules of Hooks
3. You might have more than one copy of React in the same app
See https://fb.me/react-invalid-hook-call for tips about how to debug and fix this problem.
```